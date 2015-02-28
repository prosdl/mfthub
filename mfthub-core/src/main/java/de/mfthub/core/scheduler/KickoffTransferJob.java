package de.mfthub.core.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KickoffTransferJob implements Job {
   private static Logger LOG = LoggerFactory.getLogger(KickoffTransferJob.class);

   @Override
   public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
      LOG.info("Starting job: '{}'", jobExecutionContext.getJobDetail().getKey());
      
   }

}
