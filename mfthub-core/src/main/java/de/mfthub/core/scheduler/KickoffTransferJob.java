package de.mfthub.core.scheduler;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.util.Assert;

import de.mfthub.core.async.listener.MftQueues;
import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.enums.DeliveryState;
import de.mfthub.model.repository.DeliveryRepository;
import de.mfthub.model.repository.TransferRepository;

public class KickoffTransferJob implements Job {
   private static Logger LOG = LoggerFactory
         .getLogger(KickoffTransferJob.class);

   @Autowired
   private JmsTemplate jmsTemplate;
   
   @Autowired
   private DeliveryRepository deliveryRepository;
   
   @Autowired
   private TransferRepository transferRepository;

   @Override
   public void execute(JobExecutionContext jobExecutionContext)
         throws JobExecutionException {
      LOG.info("Starting job: '{}'", jobExecutionContext.getJobDetail()
            .getKey());
      final String transferUUID = jobExecutionContext.getJobDetail().getJobDataMap().getString("transfer.uuid");
      
      final Transfer transfer = transferRepository.findOne(transferUUID);
      
      if (transfer == null) {
         throw new JobExecutionException(String.format("No transfer found for uuid='%s'.", transferUUID));
      }
      
      final Delivery delivery = new Delivery();
      delivery.setInitiated(new Date());
      delivery.setState(DeliveryState.INITIATED);
      delivery.setTransfer(transfer);
      try {
         deliveryRepository.save(delivery);
      } catch (Exception e) {
         throw new JobExecutionException(String.format(
               "Exception while trying to create a delivery for transfer '%s'",
               transferUUID), e);
      }
      
      Assert.notNull(delivery.getUuid());
      
      jmsTemplate.send(MftQueues.INBOUND, new MessageCreator() {
         @Override
         public Message createMessage(Session session) throws JMSException {
            TextMessage textMessage = session.createTextMessage(delivery.getUuid());
            textMessage.setStringProperty("transfer.uuid", transferUUID);
            return textMessage;
         }
      });
   }

}
