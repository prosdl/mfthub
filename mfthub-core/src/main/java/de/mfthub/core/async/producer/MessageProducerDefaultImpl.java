package de.mfthub.core.async.producer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import de.mfthub.core.async.MftQueues;

@Component
public class MessageProducerDefaultImpl implements MessageProducer {

   @Autowired
   private JmsTemplate jmsTemplate;
   
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
}
