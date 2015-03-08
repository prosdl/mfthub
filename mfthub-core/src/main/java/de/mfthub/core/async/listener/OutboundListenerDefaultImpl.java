package de.mfthub.core.async.listener;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
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
public class OutboundListenerDefaultImpl implements OutboundListener {
   private static final Logger LOG = LoggerFactory
         .getLogger(OutboundListenerDefaultImpl.class);

   @Autowired
   private TransferExecutor transferExecutor;

   @Autowired
   private MessageProducer messageProducer;

   @Override
   @JmsListener(destination = MftQueues.OUTBOUND, containerFactory = "defaultJmsListenerContainerFactory")
   public void send(final String deliveryUuid) throws Exception {
      LOG.info("{} received message: delivery.uuid='{}'", MftQueues.OUTBOUND,
            deliveryUuid);

      MDC.put("delivery", MftQueues.OUTBOUND + ":" + deliveryUuid.substring(0, 5));
      try {
         transferExecutor.send(deliveryUuid);
      } catch (NonRecoverableErrorException e) {
         throw e;
      } catch (RecoverableErrorException e) {
         messageProducer.sendToRedeliveryQueue(deliveryUuid, DeliveryState.OUTBOUND_READY, MftQueues.OUTBOUND);
         throw e;
      } catch (TransferExcecutionException e) {
         throw e;
      } catch (Exception e) {
         throw e;
      }      
   }

}
