package de.mfthub.transfer.exec;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import static org.springframework.util.Assert.*;
import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.EndpointConfiguration;
import de.mfthub.model.entities.FileSelector;
import de.mfthub.model.entities.LocalCpEndpointConfiguration;
import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.TransferClientType;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;
import de.mfthub.transfer.api.TransferClient;
import de.mfthub.transfer.api.TransferClient.TransferClientFeature;
import de.mfthub.transfer.exception.TransmissionException;
import de.mfthub.transfer.exception.TransmissionMisconfigurationException;
import de.mfthub.transfer.impl.LocalFilecopyTransferClient;

public class TransferExecutor {
   private static Logger LOG = LoggerFactory.getLogger(TransferExecutor.class);
   private static Map<String, TransferClient<?>> transferClientMap = initTransferClientMap();

   private static Map<String, TransferClient<?>> initTransferClientMap() {
      return ImmutableMap
            .<String, TransferClient<?>> builder()
            .put(LocalFilecopyTransferClient.class.getName(),
                  new LocalFilecopyTransferClient()).build();
   }

   public TransferExecutor() {

   }

   private TransferClient<? extends EndpointConfiguration> getTransferClient(
         TransferClientType type) {
      return transferClientMap.get(type.getImplementingClass());
   }

   public void receiveSanityCheck(Transfer transfer)
         throws TransmissionMisconfigurationException {
      notNull(transfer);
      notNull(transfer.getSource());

      Endpoint source = transfer.getSource();
      TransferClient<?> transferClient = getTransferClient(source
            .getTransferClientType());

      if (!transferClient
            .supportsFeature(TransferClientFeature.TF_SUPPORTS_RECEIVE_FILES)) {
         throw new TransmissionMisconfigurationException(
               String.format(
                     "This transfer client ('%s') does not support receiving files from a source endpoint.",
                     transferClient.toString()));
      }

      if (transfer.getTransferSendPolicies().contains(
            TransferSendPolicies.PG_LEGACY_LOCK)) {
         if (!transferClient
               .supportsFeature(TransferClientFeature.TF_SUPPORTS_REMOVE_FILES)) {
            throw new TransmissionMisconfigurationException(String.format(
                  "This transfer client ('%s') does not support PG locking.",
                  transferClient.toString()));
         }
      }

   }

   public void receive(Transfer transfer) throws TransmissionException,
         TransmissionMisconfigurationException {
      receiveSanityCheck(transfer);

      // TODO aus DB
      UUID deliveryUUID = UUID.randomUUID();
      Delivery delivery = new Delivery();
      delivery.setUuid(deliveryUUID.toString());
      delivery.setTransfer(transfer);

      Endpoint source = transfer.getSource();
      TransferClient<? extends EndpointConfiguration> transferClient = getTransferClient(source
            .getTransferClientType());
      transferClient.setConfiguration(source.getEndpointConfiguration());

      if (transfer.getTransferSendPolicies().contains(
            TransferSendPolicies.PG_LEGACY_LOCK)) {
         // TODO ...
      }

      transferClient.receive(source, delivery,
            transfer.getTransferReceivePolicies());

      if (transfer.getTransferSendPolicies().contains(
            TransferSendPolicies.PG_LEGACY_LOCK)) {
         // TODO ...
      }
   }

   public void send(Delivery delivery) throws TransmissionException {
      notNull(delivery);
      notNull(delivery.getTransfer());
      notEmpty(delivery.getTransfer().getTargets());

      Transfer transfer = delivery.getTransfer();

      for (Endpoint target : transfer.getTargets()) {
         TransferClient<?> transferClient = getTransferClient(target
               .getTransferClientType());
         transferClient.send(target, delivery,
               transfer.getTransferSendPolicies());
      }

   }

   public static void main(String[] args) throws Exception {

      TransferExecutor te = new TransferExecutor();
      
      TransferClientType type = new TransferClientType();
      type.setImplementingClass(LocalFilecopyTransferClient.class.getName());

      Endpoint source = new Endpoint();
      source.setEndpointKey("LOCALHOST_TMP_CP");
      LocalCpEndpointConfiguration conf = new LocalCpEndpointConfiguration();
      conf.setDirectory("/tmp");
      source.setEndpointConfiguration(conf);
      source.setTransferClientType(type);
      
      Endpoint target = new Endpoint();
      target.setEndpointKey("DUMMY");
      target.setEndpointConfiguration(new LocalCpEndpointConfiguration());
      target.setTransferClientType(type);
      
      Transfer transfer = new Transfer();
      transfer.setFileSelector(new FileSelector());
      transfer.setSource(source);
      transfer.setTargets(Arrays.asList(target));
      transfer.setTransferReceivePolicies(ImmutableSet.<TransferReceivePolicies>of(TransferReceivePolicies.OVERWRITE_EXISTING));
      transfer.setUuid(UUID.randomUUID().toString());

      te.receive(transfer);
   }

}
