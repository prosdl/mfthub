package de.mfthub.core.schedule;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.mfthub.core.schedule.QuartzTest.TestConf;
import de.mfthub.core.scheduler.AutowiringSpringBeanJobFactory;

@SpringApplicationConfiguration(classes = TestConf.class)
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class QuartzTest {

   @Configuration
   public static class TestConf {
      @Autowired
      private ApplicationContext applicationContext;
      
      @Bean
      public SchedulerFactoryBean scheduler() throws SchedulerException {
         SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();
         AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
         jobFactory.setApplicationContext(applicationContext);
         quartzScheduler.setJobFactory(jobFactory);         
         return quartzScheduler;
      }
      
      @Bean
      public MyService myService() {
         return new MyService();
      }
   }
   
   public static class MyService {
      @Autowired
      private ApplicationContext ctx;
      public void hello() {
         System.out.println("hello; " + ctx);
      }
   }

   public static class MyJob implements Job {
      
      @Autowired
      private MyService myService;

      @Override
      public void execute(JobExecutionContext jobExecutionContext)
            throws JobExecutionException {
         System.out.println(String.format("Running ... %tr", new Date()));
         myService.hello();
      }

   }

   @Autowired
   private Scheduler scheduler;

   @Test
   public void upAndRunning() throws SchedulerException, InterruptedException {
      Assert.assertNotNull(scheduler);
      JobDataMap map = new JobDataMap();
      JobDetail job = JobBuilder.newJob(MyJob.class)
            .withIdentity("myJob", "group1").usingJobData(map).build();
      Trigger trigger = newTrigger().forJob(job)
            .withSchedule(cronSchedule("0/1 * * * * ?")).build();
      scheduler.scheduleJob(job, trigger);
      scheduler.start();
      Thread.sleep(5000);
      scheduler.standby();
   }

}
