package de.mfthub.core.scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.mfthub.model.entities.Transfer;

@Service
public class MftSchedulerImpl implements MftScheduler {
   public static final String QUARTZ_DEFAULT_GROUP = "mftjobs";
   public static final String QUARTZ_REDELIVERY_JOBID = "redelivery";
   
   @Autowired
   private Scheduler scheduler;
   
   @Value("${mft.scheduler.redelivery.cron}")
   private String redeliveryCron;
   
   
   @Override
   public void scheduleTransfer(Transfer transfer) throws SchedulerException {
      
      JobDataMap jobDataMap = new JobDataMap();
      
      jobDataMap.put("transfer.uuid", transfer.getUuid());
      
      JobDetail job = JobBuilder.newJob(KickoffTransferJob.class)
            .withIdentity(transfer.getUuid(), QUARTZ_DEFAULT_GROUP)
            .usingJobData(jobDataMap)
            .build();
      
      
      TriggerBuilder<Trigger> triggerBuilder = newTrigger()
            .forJob(job)
            .withIdentity("trigger[" + transfer.getUuid() + "]");
      
      if (transfer.getTrigger().getStartAt() != null) {
         triggerBuilder = triggerBuilder.startAt(transfer.getTrigger().getStartAt());
      }
      
      if (transfer.getTrigger().getEndAt() != null) {
         triggerBuilder = triggerBuilder.endAt(transfer.getTrigger().getEndAt());
      }
      
      if (transfer.getTrigger().getCronExpresion() != null) {
         scheduler.scheduleJob(job, triggerBuilder.withSchedule(cronSchedule(transfer.getTrigger().getCronExpresion())).build());
      } else {
         scheduler.scheduleJob(job, triggerBuilder.build());
      }
            
   }

   @Override
   public void scheduleRedeliveryJob() throws SchedulerException {
      
      JobDataMap jobDataMap = new JobDataMap();
      
      
      JobDetail job = JobBuilder.newJob(RedeliveryJob.class)
            .withIdentity(QUARTZ_REDELIVERY_JOBID, QUARTZ_DEFAULT_GROUP)
            .usingJobData(jobDataMap)
            .build();
      Trigger trigger = newTrigger()
            .forJob(job)
            .withIdentity("trigger[" + QUARTZ_REDELIVERY_JOBID + "]")
            .withSchedule(cronSchedule(redeliveryCron))
            .build();
      scheduler.scheduleJob(job, trigger);
   }

   
   /* (non-Javadoc)
    * @see de.mfthub.core.scheduler.MftScheduler#startScheduler()
    */
   @Override
   public void startScheduler() throws SchedulerException {
      scheduler.start();
   }
   
   /* (non-Javadoc)
    * @see de.mfthub.core.scheduler.MftScheduler#standbyScheduler()
    */
   @Override
   public void standbyScheduler() throws SchedulerException {
      scheduler.standby();
   }
   
   /* (non-Javadoc)
    * @see de.mfthub.core.scheduler.MftScheduler#safeShutdownScheduler()
    */
   @Override
   public void safeShutdownScheduler() throws SchedulerException {
      scheduler.shutdown(true);
   }
   
   
}
