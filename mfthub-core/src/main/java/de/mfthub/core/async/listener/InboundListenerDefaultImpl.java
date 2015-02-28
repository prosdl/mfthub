package de.mfthub.core.async.listener;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import de.mfthub.core.mediator.TransferExecutor;
import de.mfthub.model.entities.Delivery;
import de.mfthub.model.repository.DeliveryRepository;
import de.mfthub.model.util.JSON;
import de.mfthub.transfer.exception.TransmissionException;
import de.mfthub.transfer.exception.TransmissionMisconfigurationException;

@Component
@Transactional
public class InboundListenerDefaultImpl implements InboundListener {
   private static final Logger LOG = LoggerFactory
         .getLogger(InboundListenerDefaultImpl.class);

   @Autowired
   private TransferExecutor transferExecutor;

   @Autowired
   private DeliveryRepository deliveryRepository;

   @Override
   @JmsListener(destination = MftQueues.INBOUND, containerFactory = "defaultJmsListenerContainerFactory")
   public void receive(String deliveryUuid) {
      LOG.info("{} received message: delivery.uuid='{}'", MftQueues.INBOUND,
            deliveryUuid);

      Delivery delivery = deliveryRepository.findOne(deliveryUuid);

      if (delivery == null) {
         // TODO error queue
         throw new RuntimeException(String.format(
               "Delivery with uuid '%s' not found.", deliveryUuid));
      }

      try {
         transferExecutor.receive(delivery);
      } catch (TransmissionException e) {
         // TODO error queue
         throw new RuntimeException(e);
      } catch (TransmissionMisconfigurationException e) {
         // TODO error queue
         throw new RuntimeException(e);
      }
   }

}
