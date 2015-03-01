package de.mfthub.core.mediator;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

import java.util.Date;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

import de.mfthub.core.mediator.exception.TransferExcecutionException;
import de.mfthub.core.mediator.exception.TransferMisconfigurationException;
import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.EndpointConfiguration;
import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.enums.DeliveryState;
import de.mfthub.model.entities.enums.Protocol;
import de.mfthub.model.entities.enums.TransferSendPolicies;
import de.mfthub.model.repository.DeliveryRepository;
import de.mfthub.model.repository.TransferRepository;
import de.mfthub.model.util.JSON;
import de.mfthub.transfer.api.TransferClient;
import de.mfthub.transfer.exception.TransmissionException;
import de.mfthub.transfer.impl.LocalFilecopyTransferClient;

@Component
@Transactional
public class TransferExecutorImpl implements TransferExecutor {
   private static Logger LOG = LoggerFactory.getLogger(TransferExecutorImpl.class);
   private static Map<Protocol, TransferClient<?>> transferClientMap = initTransferClientMap();

   private static Map<Protocol, TransferClient<?>> initTransferClientMap() {
      return ImmutableMap.<Protocol, TransferClient<?>> builder()
            .put(Protocol.LOCAL, new LocalFilecopyTransferClient()).build();
   }
   
   @Autowired
   private TransferRepository transferRepository;
   
   @Autowired
   private DeliveryRepository deliveryRepository;

   public TransferExecutorImpl() {

   }

   private TransferClient<? extends EndpointConfiguration> getTransferClient(
         Protocol protocol) {
      return transferClientMap.get(protocol);
   }
   
   @Override
   public String createDeliveryForTransfer(String transferUUID) throws TransferExcecutionException  {
      
      final Transfer transfer = transferRepository.findOne(transferUUID);
      
      if (transfer == null) {
         throw new TransferExcecutionException(String.format("No transfer found for uuid='%s'.", transferUUID));
      }
      
      final Delivery delivery = new Delivery();
      delivery.setInitiated(new Date());
      delivery.setState(DeliveryState.INITIATED);
      delivery.setTransfer(transfer);
      try {
         deliveryRepository.save(delivery);
      } catch (Exception e) {
         throw new TransferExcecutionException(String.format(
               "Exception while trying to create a delivery for transfer '%s'",
               transfer.getUuid()), e);
      }
      
      return delivery.getUuid();
        
   }

   /* (non-Javadoc)
    * @see de.mfthub.core.mediator.TransferExecutor#receive(de.mfthub.model.entities.Transfer)
    */
   @Override
   public void receive(Delivery delivery) throws TransmissionException,
         TransferMisconfigurationException {
      LOG.info("Starting reception phase of delivery {}. Details:\n{}", delivery.getUuid(), JSON.toJson(delivery));
      
      Transfer transfer = delivery.getTransfer();
      Endpoint source = transfer.getSource();
      TransferClient<?> transferClient = getTransferClient(source.getProtocol());

      new TransferSanityCheck(transfer, transferClient).receiveSanityCheck();

      transferClient.setConfiguration(source.getEndpointConfiguration());

      if (transfer.getTransferSendPolicies().contains(
            TransferSendPolicies.LOCKSTRATEGY_PG_LEGACY)) {
         // TODO ...
      }

      transferClient.receive(source, delivery,
            transfer.getTransferReceivePolicies());

      if (transfer.getTransferSendPolicies().contains(
            TransferSendPolicies.LOCKSTRATEGY_PG_LEGACY)) {
         // TODO ...
      }
   }

   /* (non-Javadoc)
    * @see de.mfthub.core.mediator.TransferExecutor#send(de.mfthub.model.entities.Delivery)
    */
   @Override
   public void send(Delivery delivery) throws TransmissionException {
      notNull(delivery);
      notNull(delivery.getTransfer());
      notEmpty(delivery.getTransfer().getTargets());

      Transfer transfer = delivery.getTransfer();

      for (Endpoint target : transfer.getTargets()) {
         TransferClient<?> transferClient = getTransferClient(target
               .getProtocol());
         transferClient.send(target, delivery,
               transfer.getTransferSendPolicies());
      }

   }
}
