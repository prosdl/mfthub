package de.mfthub.core.async.listener;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import de.mfthub.core.async.MftQueues;

@Component
@Transactional
public class ProcessingListenerDefaultImpl implements ProcessingListener {
   private static final Logger LOG = LoggerFactory
         .getLogger(ProcessingListenerDefaultImpl.class);

   @Override
   @JmsListener(destination = MftQueues.PROCESSING, containerFactory = "defaultJmsListenerContainerFactory")
   @SendTo(MftQueues.OUTBOUND)
   public String receive(String deliveryUuid) {
      LOG.info("{} received message: delivery.uuid='{}'", MftQueues.PROCESSING,
            deliveryUuid);
      
      return deliveryUuid;
   }

}
