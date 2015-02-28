package de.mfthub.core.conf;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import de.mfthub.core.scheduler.AutowiringSpringBeanJobFactory;

@Configuration
public class CoreQuartzConfiguration {
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
}
