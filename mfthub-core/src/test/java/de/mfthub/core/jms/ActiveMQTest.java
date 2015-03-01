package de.mfthub.core.jms;

import java.util.concurrent.CountDownLatch;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.mfthub.core.async.MftQueues;
import de.mfthub.core.async.listener.InboundListener;
import de.mfthub.core.async.listener.InboundListenerDefaultImpl;
import de.mfthub.core.jms.ActiveMQTest.TestConf;

@SpringApplicationConfiguration(classes = TestConf.class)
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ActiveMQTest {
   private static final String JMS_QUEUE = "jms.queue";

   @Configuration
   @EnableJms
   public static class TestConf {
      

      @Bean
      public SimpleReceiver simpleReceiver() {
         return new SimpleReceiver();
      }
      
      @Bean
      public InboundListener inboundListener() {
         return new InboundListenerDefaultImpl();
      }
      
      @Bean
      public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory() {
         DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = 
               new DefaultJmsListenerContainerFactory();
         defaultJmsListenerContainerFactory.setConnectionFactory(connectionFactory());
         defaultJmsListenerContainerFactory.setConcurrency("3-10");
         return defaultJmsListenerContainerFactory;
      }
      
      @Bean
      public JmsTemplate jmsTemplate() {
         JmsTemplate jmsTemplate = new JmsTemplate();
         jmsTemplate.setDefaultDestination(new ActiveMQQueue(JMS_QUEUE));
         jmsTemplate.setConnectionFactory(connectionFactory());
         return jmsTemplate;
      }

      @Bean
      public ActiveMQConnectionFactory connectionFactory() {
         ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
         activeMQConnectionFactory.setBrokerURL("vm://localhost");
         return activeMQConnectionFactory;
      }
   }

   @Component
   public static class SimpleReceiver {
      private CountDownLatch latch = new CountDownLatch(1);
      @JmsListener(destination = "jms.queue", containerFactory="defaultJmsListenerContainerFactory")
      public void receive(String message) {
         System.out.println("received: " + message);
         this.latch.countDown();
      }
   }

   @Autowired
   private JmsTemplate jmsTemplate;
   
   @Test
   public void sendSimpleMessage() {
      System.out.println("Sending ....");
      jmsTemplate.convertAndSend(JMS_QUEUE, "foo");
      jmsTemplate.convertAndSend(MftQueues.INBOUND, "inbound");
      jmsTemplate.send(MftQueues.INBOUND, new MessageCreator() {
         @Override
         public Message createMessage(Session session) throws JMSException {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("cron message");
//            textMessage.setStringProperty(ScheduledMessage.AMQ_SCHEDULED_CRON, "0/1 * * * *");
            textMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, 50);
            return textMessage;
         }
      });
      System.out.println("Done.");
      try {
         Thread.sleep(5000);
      } catch (InterruptedException e) {
      }
   }
}
