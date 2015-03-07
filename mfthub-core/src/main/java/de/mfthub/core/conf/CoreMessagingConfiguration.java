package de.mfthub.core.conf;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ErrorHandler;

import de.mfthub.core.async.MftQueues;
import de.mfthub.core.async.listener.MftErrorHandler;

@Configuration
@EnableJms
public class CoreMessagingConfiguration {

   @Bean
   public ErrorHandler errorHandler() {
      return new MftErrorHandler();
   }

   @Bean
   public DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory() {
      DefaultJmsListenerContainerFactory defaultJmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();
      defaultJmsListenerContainerFactory
            .setConnectionFactory(connectionFactory());
      defaultJmsListenerContainerFactory.setConcurrency("3-10");
      defaultJmsListenerContainerFactory.setErrorHandler(errorHandler());
      return defaultJmsListenerContainerFactory;
   }

   @Bean
   public JmsTemplate jmsTemplate() {
      JmsTemplate jmsTemplate = new JmsTemplate();
      jmsTemplate.setDefaultDestination(new ActiveMQQueue(MftQueues.INBOUND));
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
