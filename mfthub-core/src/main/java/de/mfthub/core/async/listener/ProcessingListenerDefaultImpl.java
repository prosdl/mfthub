package de.mfthub.core.async.listener;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import de.mfthub.core.async.MftQueues;
import de.mfthub.core.mediator.TransferExecutor;
import de.mfthub.core.mediator.exception.TransferExcecutionException;

@Component
@Transactional
public class ProcessingListenerDefaultImpl implements ProcessingListener {
   private static final Logger LOG = LoggerFactory
         .getLogger(ProcessingListenerDefaultImpl.class);

   @Autowired
   private TransferExecutor transferExecutor;
   
   @Override
   @JmsListener(destination = MftQueues.PROCESSING, containerFactory = "defaultJmsListenerContainerFactory")
   @SendTo(MftQueues.OUTBOUND)
   public String receive(String deliveryUuid) {
      LOG.info("{} received message: delivery.uuid='{}'", MftQueues.PROCESSING,
            deliveryUuid);
      
      try {
         transferExecutor.prepareProcessing(deliveryUuid);
         transferExecutor.process(deliveryUuid);
         transferExecutor.prepareSend(deliveryUuid);
      } catch (TransferExcecutionException e) {
         // TODO Auto-generated catch block
         throw new RuntimeException(e);
      }
      
      return deliveryUuid;
   }

}
