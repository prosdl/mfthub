package de.mfthub.core.scheduler;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import de.mfthub.core.async.MftQueues;

@Component
public class RedeliveryJob implements Job {
   private static Logger LOG = LoggerFactory
         .getLogger(RedeliveryJob.class);

   @Autowired
   private JmsTemplate jmsTemplate;
   
   @Override
   public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
      LOG.info("Starting redelivery job: '{}'", jobExecutionContext.getJobDetail()
            .getKey());
      jmsTemplate.setReceiveTimeout(100);
      TextMessage msg = (TextMessage) jmsTemplate.receive(MftQueues.REDELIVERY);
      if (msg != null) {
         try {
            LOG.info("msg: {}  -- {} -- {}", msg.getText(), msg.getLongProperty("when"), msg.getStringProperty("next-state"));
         } catch (JMSException e) {
            throw new JobExecutionException(e);
         }
      }
   }

}
