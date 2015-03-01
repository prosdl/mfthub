package de.mfthub.core.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import de.mfthub.core.async.producer.MessageProducer;
import de.mfthub.core.mediator.TransferExecutor;
import de.mfthub.core.mediator.exception.TransferExcecutionException;

public class KickoffTransferJob implements Job {
   private static Logger LOG = LoggerFactory
         .getLogger(KickoffTransferJob.class);

   @Autowired
   private TransferExecutor transferExecutor;
   
   @Autowired
   private MessageProducer messageProducer;

   @Override
   public void execute(JobExecutionContext jobExecutionContext)
         throws JobExecutionException {
      LOG.info("Starting transfer: '{}'", jobExecutionContext.getJobDetail()
            .getKey());
      final String transferUuid = jobExecutionContext.getJobDetail().getJobDataMap().getString("transfer.uuid");
      
      final String deliveryUuid;
      try {
         deliveryUuid = transferExecutor.createDeliveryForTransfer(transferUuid);
      } catch (TransferExcecutionException e) {
         LOG.error("Problem while trying to create delivery.",e);
         throw new JobExecutionException(e);
      }
      
      Assert.notNull(deliveryUuid);
      
      messageProducer.queueDeliveryForInboundMessage(deliveryUuid, transferUuid);
   }

}
