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
import de.mfthub.model.entities.EndpointConfLocalCp;
import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.TransferClientType;
import de.mfthub.model.entities.enums.Protocol;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;
import de.mfthub.transfer.api.TransferClient;
import de.mfthub.transfer.api.TransferClient.TransferClientFeature;
import de.mfthub.transfer.exception.TransmissionException;
import de.mfthub.transfer.exception.TransmissionMisconfigurationException;
import de.mfthub.transfer.impl.LocalFilecopyTransferClient;

public class TransferExecutor {
   private static Logger LOG = LoggerFactory.getLogger(TransferExecutor.class);
   private static Map<Protocol, TransferClient<?>> transferClientMap = initTransferClientMap();

   private static Map<Protocol, TransferClient<?>> initTransferClientMap() {
      return ImmutableMap
            .<Protocol, TransferClient<?>> builder()
            .put(Protocol.LOCAL,
                  new LocalFilecopyTransferClient()).build();
   }

   public TransferExecutor() {

   }

   private TransferClient<? extends EndpointConfiguration> getTransferClient(Protocol protocol) {
      return transferClientMap.get(protocol);
   }

 
   public void receive(Transfer transfer) throws TransmissionException,
         TransmissionMisconfigurationException {
      Endpoint source = transfer.getSource();
      TransferClient<? extends EndpointConfiguration> transferClient = getTransferClient(source
            .getProtocol());
      
      new TransferSanityCheck(transfer, transferClient).receiveSanityCheck();
      
      // TODO aus DB
      UUID deliveryUUID = UUID.randomUUID();
      Delivery delivery = new Delivery();
      delivery.setUuid(deliveryUUID.toString());
      delivery.setTransfer(transfer);

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

   public static void main(String[] args) throws Exception {

      TransferExecutor te = new TransferExecutor();
      
      Endpoint source = Protocol.buildEndpointFromURI("local:///tmp");

      Endpoint target = Protocol.buildEndpointFromURI("local:///tmp/foo");
      
      Transfer transfer = new Transfer();
      transfer.setFileSelector(new FileSelector());
      transfer.setSource(source);
      transfer.setTargets(Arrays.asList(target));
      transfer.setTransferReceivePolicies(ImmutableSet.<TransferReceivePolicies>of(TransferReceivePolicies.LOCKSTRATEGY_PG_LEGACY));
      transfer.setUuid(UUID.randomUUID().toString());

      te.receive(transfer);
   }

}
