package de.mfthub.core.async.listener;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import de.mfthub.core.async.MftQueues;
import de.mfthub.core.async.producer.MessageProducer;
import de.mfthub.core.mediator.TransferExecutor;
import de.mfthub.core.mediator.exception.NonRecoverableErrorException;
import de.mfthub.core.mediator.exception.RecoverableErrorException;
import de.mfthub.core.mediator.exception.TransferExcecutionException;
import de.mfthub.model.entities.enums.DeliveryState;

@Component
@Transactional
public class InboundListenerDefaultImpl implements InboundListener {
   private static final Logger LOG = LoggerFactory
         .getLogger(InboundListenerDefaultImpl.class);

   @Autowired
   private TransferExecutor transferExecutor;

   @Autowired
   private MessageProducer messageProducer;

   @Override
   @JmsListener(destination = MftQueues.INBOUND, containerFactory = "defaultJmsListenerContainerFactory")
   @SendTo(MftQueues.PROCESSING)
   public String receive(final String deliveryUuid) throws Exception {
      LOG.info("{} received message: delivery.uuid='{}'", MftQueues.INBOUND,
            deliveryUuid);

      try {
         transferExecutor.receive(deliveryUuid);
      } catch (NonRecoverableErrorException e) {
         throw e;
      } catch (RecoverableErrorException e) {
         messageProducer.sendToRedeliveryQueue(deliveryUuid, DeliveryState.INITIATED, MftQueues.INBOUND);
         throw e;
      } catch (TransferExcecutionException e) {
         throw e;
      } catch (Exception e) {
         throw e;
      }

      return deliveryUuid;
   }

}
