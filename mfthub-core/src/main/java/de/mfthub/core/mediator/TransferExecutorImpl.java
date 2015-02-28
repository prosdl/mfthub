package de.mfthub.core.mediator;

import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableMap;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.EndpointConfiguration;
import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.enums.Protocol;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;
import de.mfthub.model.repository.TransferRepository;
import de.mfthub.transfer.api.TransferClient;
import de.mfthub.transfer.exception.TransmissionException;
import de.mfthub.transfer.exception.TransmissionMisconfigurationException;
import de.mfthub.transfer.impl.LocalFilecopyTransferClient;

@Component
public class TransferExecutorImpl implements TransferExecutor {
   private static Logger LOG = LoggerFactory.getLogger(TransferExecutorImpl.class);
   private static Map<Protocol, TransferClient<?>> transferClientMap = initTransferClientMap();

   private static Map<Protocol, TransferClient<?>> initTransferClientMap() {
      return ImmutableMap.<Protocol, TransferClient<?>> builder()
            .put(Protocol.LOCAL, new LocalFilecopyTransferClient()).build();
   }
   
   @Autowired
   private TransferRepository transferRepository;

   public TransferExecutorImpl() {

   }

   private TransferClient<? extends EndpointConfiguration> getTransferClient(
         Protocol protocol) {
      return transferClientMap.get(protocol);
   }

   /* (non-Javadoc)
    * @see de.mfthub.core.mediator.TransferExecutor#receive(de.mfthub.model.entities.Transfer)
    */
   @Override
   public void receive(Transfer transfer) throws TransmissionException,
         TransmissionMisconfigurationException {
      Endpoint source = transfer.getSource();
      TransferClient<?> transferClient = getTransferClient(source.getProtocol());

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

   public static void main(String[] args) throws Exception {
      TransferExecutor te = new TransferExecutorImpl();
      Transfer transfer = new Transfer.Builder("testtransfer")
            .withRandamUUID()
            .fromNamedSource("MY_SENDER", "local:///tmp/source")
            .toTargets("local:///tmp/foo")
            .files("bar/**/*.pdf")
            .usingReceivePolicies(
                  TransferReceivePolicies.LOCKSTRATEGY_PG_LEGACY).build();
      te.receive(transfer);
   }

}
