package de.mfthub.core.scheduler;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.mfthub.model.entities.Transfer;

@Service
public class MftSchedulerImpl implements MftScheduler {
   public static final String QUARTZ_DEFAULT_GROUP = "mftjobs";
   
   @Autowired
   private Scheduler scheduler;
   
   
   /* (non-Javadoc)
    * @see de.mfthub.core.scheduler.MftScheduler#scheduleTransfer(de.mfthub.model.entities.Transfer)
    */
   @Override
   public void scheduleTransfer(Transfer transfer) throws SchedulerException {
      
      JobDataMap jobDataMap = new JobDataMap();
      
      jobDataMap.put("transfer.uuid", transfer.getUuid());
      
      JobDetail job = JobBuilder.newJob(KickoffTransferJob.class)
            .withIdentity(transfer.getUuid(), QUARTZ_DEFAULT_GROUP)
            .usingJobData(jobDataMap)
            .build();
      Trigger trigger = newTrigger()
            .forJob(job)
            .withIdentity("trigger[" + transfer.getUuid() + "]")
            .withSchedule(cronSchedule(transfer.getTrigger().getCronExpresion()))
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
