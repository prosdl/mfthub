package de.mfthub.core.async.listener;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import de.mfthub.core.async.MftQueues;
import de.mfthub.core.mediator.TransferExecutor;
import de.mfthub.model.entities.Delivery;
import de.mfthub.model.repository.DeliveryRepository;
import de.mfthub.transfer.exception.TransmissionException;

@Component
@Transactional
public class OutboundListenerDefaultImpl implements OutboundListener {
   private static final Logger LOG = LoggerFactory
         .getLogger(OutboundListenerDefaultImpl.class);

   @Autowired
   private TransferExecutor transferExecutor;

   @Autowired
   private DeliveryRepository deliveryRepository;

   @Override
   @JmsListener(destination = MftQueues.OUTBOUND, containerFactory = "defaultJmsListenerContainerFactory")
   public void send(String deliveryUuid) {
      LOG.info("{} received message: delivery.uuid='{}'", MftQueues.OUTBOUND,
            deliveryUuid);

      Delivery delivery = deliveryRepository.findOne(deliveryUuid);

      if (delivery == null) {
         // TODO error queue
         throw new RuntimeException(String.format(
               "Delivery with uuid '%s' not found.", deliveryUuid));
      }

      try {
         transferExecutor.send(delivery);
      } catch (TransmissionException e) {
         // TODO error queue
         throw new RuntimeException(e);
      }
   }

}
