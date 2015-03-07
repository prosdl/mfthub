package de.mfthub.core.async.producer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import de.mfthub.core.async.MftQueues;
import de.mfthub.model.entities.enums.DeliveryState;
import de.mfthub.model.repository.DeliveryRepository;

@Component
public class MessageProducerDefaultImpl implements MessageProducer {

   @Autowired
   private JmsTemplate jmsTemplate;
   
   @Autowired
   private DeliveryRepository deliveryRepository;
   
   @Value("${mft.jms.redelivery.delay-in-secs}")
   private int deliveryDelayInSecs;
   
   @Override
   public void queueDeliveryForInboundMessage(final String deliveryUuid, final String transferUuid) {
      jmsTemplate.send(MftQueues.INBOUND, new MessageCreator() {
         @Override
         public Message createMessage(Session session) throws JMSException {
            TextMessage textMessage = session.createTextMessage(deliveryUuid);
            textMessage.setStringProperty("transfer.uuid", transferUuid);
            return textMessage;
         }
      });
   }
   
   
   @Override
   @Transactional
   public void redeliverMessage(String targetQueue, String deliveryUuid, String nextState) {

      deliveryRepository.updateDeliveryState(deliveryUuid,
            DeliveryState.valueOf(nextState),
            "Requeue from redelivery-scheduler.", null);
      jmsTemplate.convertAndSend(targetQueue, deliveryUuid);
   }
   
   
   @Override
   public void sendToRedeliveryQueue(final String deliveryUuid, final DeliveryState state, final String toQueue) {
      jmsTemplate.send(MftQueues.REDELIVERY, new MessageCreator() {

         @Override
         public Message createMessage(Session session) throws JMSException {
            TextMessage msg = session.createTextMessage(deliveryUuid);
            msg.setLongProperty("when",
                  System.currentTimeMillis() + 1000 * deliveryDelayInSecs);
            msg.setStringProperty("next-state",
                  state.name());
            msg.setStringProperty("next-queue", toQueue);
            return msg;
         }
      });      
   }
}
