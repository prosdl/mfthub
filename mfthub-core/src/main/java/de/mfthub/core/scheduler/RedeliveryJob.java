package de.mfthub.core.scheduler;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.transaction.Transactional;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import de.mfthub.core.async.MftQueues;
import de.mfthub.core.async.producer.MessageProducer;
import de.mfthub.model.entities.enums.DeliveryState;
import de.mfthub.model.repository.DeliveryRepository;

@Component
@Transactional
public class RedeliveryJob implements Job {
   private static Logger LOG = LoggerFactory.getLogger(RedeliveryJob.class);

   @Autowired
   private JmsTemplate jmsTemplate;

   @Autowired
   private MessageProducer messageProducer;

   @Override
   public void execute(JobExecutionContext jobExecutionContext)
         throws JobExecutionException {
      LOG.info("Starting redelivery job: '{}'", jobExecutionContext
            .getJobDetail().getKey());
      jmsTemplate.setReceiveTimeout(100);
      TextMessage msg = (TextMessage) jmsTemplate.receiveSelected(
            MftQueues.REDELIVERY, "when <= " + System.currentTimeMillis());
      if (msg != null) {
         String deliveryUuid;
         Long when;
         String nextState;
         String nextQueue;
         try {
            deliveryUuid = msg.getText();
            when = msg.getLongProperty("when");
            nextState = msg.getStringProperty("next-state");
            nextQueue = msg.getStringProperty("next-queue");
         } catch (JMSException e) {
            throw new JobExecutionException(e);
         }
         LOG.info("Delivery will be requeued to {}. Delivery is: uuid={}  -- when={} [{}] -- nextState={}",
               nextQueue, deliveryUuid, when, new Date(when), nextState);
         messageProducer.requeue(nextQueue, deliveryUuid, nextState);
      } else {
         LOG.info("No redeliveries queued.");
      }
   }

}
