package de.mfthub.core.mediator;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

import de.mfthub.core.mediator.exception.TransferExcecutionException;
import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.EndpointConfiguration;
import de.mfthub.model.entities.Processing;
import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.enums.DeliveryState;
import de.mfthub.model.entities.enums.ProcessingType;
import de.mfthub.model.entities.enums.Protocol;
import de.mfthub.model.entities.enums.TransferSendPolicies;
import de.mfthub.model.repository.DeliveryRepository;
import de.mfthub.model.repository.TransferRepository;
import de.mfthub.model.util.JSON;
import de.mfthub.processing.api.Processor;
import de.mfthub.processing.api.exception.ProcessorException;
import de.mfthub.processing.impl.CompressProcessor;
import de.mfthub.storage.folder.MftFolder;
import de.mfthub.storage.folder.MftPathException;
import de.mfthub.storage.nio.MoveOrCopyFilesVisitor;
import de.mfthub.transfer.api.TransferClient;
import de.mfthub.transfer.api.TransferReceiptInfo;
import de.mfthub.transfer.api.TransferSendInfo;
import de.mfthub.transfer.exception.TransmissionException;
import de.mfthub.transfer.impl.local.LocalFilecopyTransferClient;
import de.mfthub.transfer.impl.ssh.ScpTransferClient;

@Component
@Transactional
public class TransferExecutorImpl implements TransferExecutor {
   private static Logger LOG = LoggerFactory
         .getLogger(TransferExecutorImpl.class);
   private static Map<Protocol, Class<? extends TransferClient<?>>> transferClientMap = initTransferClientMap();
   private static Map<ProcessingType, Class<? extends Processor>> processorMap = initProcessorMap();

   private static Map<Protocol, Class<? extends TransferClient<?>>> initTransferClientMap() {
      return ImmutableMap.<Protocol, Class<? extends TransferClient<?>>> builder()
            .put(Protocol.LOCAL, LocalFilecopyTransferClient.class)
            .put(Protocol.SCP, ScpTransferClient.class)
            .build();
   }

   private static Map<ProcessingType, Class<? extends Processor>> initProcessorMap() {
      return ImmutableMap.<ProcessingType, Class<? extends Processor>> builder()
            .put(ProcessingType.COMPRESS, CompressProcessor.class).build();
   }

   @Autowired
   private TransferRepository transferRepository;

   @Autowired
   private DeliveryRepository deliveryRepository;
   
   @Autowired
   private RecoveryDecisionMaker recoveryDecisionMaker;

   public TransferExecutorImpl() {

   }

   private static TransferClient<?> getTransferClient(Protocol protocol,
         EndpointConfiguration conf) {
      Class<? extends TransferClient<?>> clientClass = transferClientMap
            .get(protocol);
      if (clientClass == null) {
         throw new IllegalStateException("No registered client for protocol: "
               + protocol);
      }
      try {
         return clientClass.getDeclaredConstructor(conf.getClass()).newInstance(conf);
      } catch (InstantiationException | IllegalAccessException
            | IllegalArgumentException | InvocationTargetException
            | NoSuchMethodException | SecurityException e) {
         throw new IllegalStateException(
               "Couldn't create instance for protocol: " + protocol, e);
      }
   }
   
   private static Processor getProcessor(ProcessingType type) {
      Class<? extends Processor> classProcessor = processorMap.get(type);
      if (classProcessor == null) {
         throw new IllegalStateException("No registered processor for type: "+ type);
      }
      
      try {
         return classProcessor.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
         throw new IllegalStateException("Couldn't create instance for type: "+ type, e);
      }
   }

   @Override
   public String createDeliveryForTransfer(String transferUUID)
         throws TransferExcecutionException {

      final Transfer transfer = transferRepository.findOne(transferUUID);

      if (transfer == null) {
         throw new TransferExcecutionException(String.format(
               "No transfer found for uuid='%s'.", transferUUID));
      }

      final Delivery delivery = new Delivery();
      delivery.setInitiated(new Date());
      delivery.setState(null);
      delivery.setTransfer(transfer);
      try {
         deliveryRepository.save(delivery);
         deliveryRepository.updateDeliveryState(delivery,
               DeliveryState.INITIATED, "Created", null);
      } catch (Exception e) {
         throw new TransferExcecutionException(String.format(
               "Exception while trying to create a delivery for transfer '%s'",
               transfer.getUuid()), e);
      }
      
      LOG.info("Created new delivery with uuid={}", delivery.getUuid());

      return delivery.getUuid();
   }

   @Override
   public void receive(String deliveryUuid) throws TransferExcecutionException {
      Delivery delivery = getDelivery(deliveryUuid);
      receive(delivery);
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.mfthub.core.mediator.TransferExecutor#receive(de.mfthub.model.entities
    * .Transfer)
    */
   @Override
   public void receive(Delivery delivery) throws TransferExcecutionException {
      LOG.info("Starting receipt phase of delivery {}. Details:\n{}",
            delivery.getUuid(), JSON.toJson(delivery));

      Transfer transfer = delivery.getTransfer();
      Endpoint source = transfer.getSource();
      TransferClient<?> transferClient = getTransferClient(source.getProtocol(), source.getEndpointConfiguration());

      new TransferSanityCheck(transfer, transferClient).receiveSanityCheck();

      if (transfer.getTransferSendPolicies().contains(
            TransferSendPolicies.LOCKSTRATEGY_PG_LEGACY)) {
         // TODO ...
      }

      TransferReceiptInfo info = null;
      try {
         info = transferClient.receive(source, delivery,
               transfer.getTransferReceivePolicies());
      } catch (TransmissionException e) {
         recoveryDecisionMaker.throwTEE(e, delivery);
      }

      if (transfer.getTransferSendPolicies().contains(
            TransferSendPolicies.LOCKSTRATEGY_PG_LEGACY)) {
         // TODO ...
      }

      deliveryRepository.updateDeliveryState(delivery, DeliveryState.FILES_INBOUND,
            String.format("Copied %d files to inbound folder", info.getNumberOfFiles()),
            String.format("%s copied %d files (%,d bytes) in %.2f secs (%,.3f MBytes/sec) to folder: '%s'",
                  source.getProtocol(),
                  info.getNumberOfFiles(),
                  info.getTotalBytes(),
                  info.getTimeNeededInMilliSecs() / 1000.0,
                  info.getTransferSpeedInMBperSec(),
                  info.getInboundFolder()));
      LOG.info("Ending receipt phase of delivery {}. Details:\n{}",
            delivery.getUuid(), JSON.toJson(delivery));
   }

   @Override
   public void send(String deliveryUuid) throws TransferExcecutionException {
      Delivery delivery = getDelivery(deliveryUuid);
      send(delivery);
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * de.mfthub.core.mediator.TransferExecutor#send(de.mfthub.model.entities
    * .Delivery)
    */
   @Override
   public void send(Delivery delivery) throws TransferExcecutionException {
      notNull(delivery);
      notNull(delivery.getTransfer());
      notEmpty(delivery.getTransfer().getTargets());
      LOG.info("Starting send phase of delivery {}. Details:\n{}",
            delivery.getUuid(), JSON.toJson(delivery));

      Transfer transfer = delivery.getTransfer();

      for (Endpoint target : transfer.getTargets()) {
         TransferClient<?> transferClient = getTransferClient(target
               .getProtocol(), target.getEndpointConfiguration());
         
         TransferSendInfo info = null;
         try {
            info = transferClient.send(target, delivery,
                  transfer.getTransferSendPolicies());
         } catch (TransmissionException e) {
            recoveryDecisionMaker.throwTEE(e, delivery);
         }


         deliveryRepository.updateDeliveryState(delivery,
               DeliveryState.FILES_SEND, 
               String.format("Copied %d files from outbound folder", info.getNumberOfFiles()),
                     String.format("%s copied %d files (%,d bytes) in %.2f secs (%,.3f MBytes/sec) from folder: '%s'",
                           target.getProtocol(),
                           info.getNumberOfFiles(),
                           info.getTotalBytes(),
                           info.getTimeNeededInMilliSecs() / 1000.0,
                           info.getTransferSpeedInMBperSec(),
                           info.getOutboundFolder()));
      }

      LOG.info("Ending send phase of delivery {}. Details:\n{}",
            delivery.getUuid(), JSON.toJson(delivery));
   }

   @Override
   public void prepareProcessing(Delivery delivery)
         throws TransferExcecutionException {
      LOG.info("Starting prepare-processing phase for delivery {}.",
            delivery.getUuid());

      MftFolder inbound;
      MftFolder processingOut;
      try {
         inbound = MftFolder.createInboundFromDelivery(delivery);
         processingOut = MftFolder.createProcessingOutFromDelivery(delivery);
      } catch (IOException | MftPathException e) {
         throw new TransferExcecutionException(String.format(
               "Error while creating mft folders for delivery %s",
               delivery.getUuid()), e);
      }

      LOG.info("copy inbound --> processing_out");
      
      MoveOrCopyFilesVisitor visitor = new MoveOrCopyFilesVisitor("**/*.*",
            inbound.getPath(), processingOut.getPath());
      try {
         Files.walkFileTree(inbound.getPath(), visitor);
      } catch (IOException e) {
         throw new TransferExcecutionException(
               String.format(
                     "Error while copying inbound folder to processing_out folder for delivery %s",
                     delivery.getUuid()), e);
      }

      deliveryRepository.updateDeliveryState(delivery,
            DeliveryState.PROCESSING_READY, 
            String.format("Copied %d files from inbound to processing-out.", visitor.getFileCount()),
            String.format(
                  "Copied %d files (%,d bytes) from '%s' to '%s'", visitor
                        .getFileCount(), visitor.getByteCount(), inbound
                        .getPath().toString(), processingOut.getPath().toString())
            );

      LOG.info(
            "Finished prepare-processing phase for delivery: {}.",
            delivery.getUuid());

   }

   @Override
   public void prepareProcessing(String deliveryUuid)
         throws TransferExcecutionException {
      prepareProcessing(getDelivery(deliveryUuid));
   }
   
   @Override
   public void prepareSend(String deliveryUuid) throws TransferExcecutionException {
      prepareSend(getDelivery(deliveryUuid));
   }
   
   @Override
   public void prepareSend(Delivery delivery) throws TransferExcecutionException {
      LOG.info("Starting prepare-send phase for delivery {}.",
            delivery.getUuid());

      // mv processing -> processingIn
      MftFolder outbound;
      MftFolder processingOut;
      try {
         outbound = MftFolder.createOutboundFromDelivery(delivery);
         processingOut = MftFolder.createProcessingOutFromDelivery(delivery);
      } catch (IOException | MftPathException e) {
         throw new TransferExcecutionException(String.format(
               "Error while creating mft folders for delivery %s",
               delivery.getUuid()), e);
      }
      try {
         Files.move(processingOut.getPath(), outbound.getPath(), StandardCopyOption.ATOMIC_MOVE);
      } catch (IOException e) {
         throw new TransferExcecutionException(String.format(
               "Error while moving processing_out --> outbound for delivery: %s",
               delivery.getUuid()), e);
      }

      deliveryRepository.updateDeliveryState(delivery,
            DeliveryState.OUTBOUND_READY, 
            "outbound folder successfully created.", null);
      LOG.info("Finished prepare-send phase for delivery {}.",
            delivery.getUuid());
   }
   
   @Override
   public void process(Delivery delivery) throws TransferExcecutionException {

      // TODO one jms message per transformation
      int step = 0;
      for (Processing processing: delivery.getTransfer().getProcessings() ) {
         step++;
         LOG.info("Starting processing step: {} for delivery {}.", step, delivery.getUuid());
         
         
         // mv processing -> processingIn
         MftFolder processingIn;
         MftFolder processingOut;
         try {
            processingIn = MftFolder.createProcessingInFromDelivery(delivery);
            processingOut = MftFolder.createProcessingOutFromDelivery(delivery);
         } catch (IOException | MftPathException e) {
            throw new TransferExcecutionException(String.format(
                  "Error while creating mft folders for delivery %s",
                  delivery.getUuid()), e);
         }
         
         try {
            // FIXME holy cow, deleting a directory in java ...
            //
            // * commons.io uses (ugh) File[] files = directory.listFiles();
            // * Guava deleted (no pun) their recursive delete with:
            //   "Deprecated. This method suffers from poor symlink detection and 
            //   race conditions. This functionality can be supported suitably only 
            //   by shelling out to an operating system command such as rm -rf or del /s. 
            //   This method is scheduled to be removed from Guava in Guava release 11.0."
            FileUtils.deleteDirectory(processingIn.getPath().toFile());
            
            // pukes if ATOMIC_MOVE not supported, which is good
            Files.move(processingOut.getPath(), processingIn.getPath(), StandardCopyOption.ATOMIC_MOVE);
         } catch (IOException e) {
            throw new TransferExcecutionException(String.format(
                  "Error while cycling processing in/out folders for delivery: %s",
                  delivery.getUuid()), e);
         }
         
         Processor processor = getProcessor(processing.getType());
         try {
            processor.processFiles(delivery);
         } catch (ProcessorException e) {
            throw new TransferExcecutionException(
                  String.format("Problem during processing with %s.", processing.getType()), e);
         }
         LOG.info("Finished processing step: {} for delivery {}.", step, delivery.getUuid());
      }

      deliveryRepository.updateDeliveryState(delivery,
            DeliveryState.PROCESSING_DONE, 
            String.format("Number of processing steps completed: %d", step), null);
   }

   @Override
   public void process(String deliveryUuid) throws TransferExcecutionException {
      process(getDelivery(deliveryUuid));
   }

   private Delivery getDelivery(String deliveryUuid)
         throws TransferExcecutionException {
      Delivery delivery = deliveryRepository.findOne(deliveryUuid);

      if (delivery == null) {
         throw new TransferExcecutionException(String.format(
               "Delivery with uuid '%s' not found.", deliveryUuid));
      }
      return delivery;
   }
}
