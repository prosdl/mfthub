package de.mfthub.core.integrity;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.mfthub.core.api.MftCoreAPI;
import de.mfthub.core.api.MftCoreAPIException;
import de.mfthub.core.async.MftQueues;
import de.mfthub.core.conf.CoreMessagingConfiguration;
import de.mfthub.core.conf.CoreQuartzConfiguration;
import de.mfthub.core.integrity.SimpleTest.TestConf;
import de.mfthub.core.mediator.TransferExecutor;
import de.mfthub.core.scheduler.MftScheduler;
import de.mfthub.model.conf.ModelJPAConfiguration;
import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.enums.ProcessingType;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;

@SpringApplicationConfiguration(classes = TestConf.class)
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleTest {

   @Configuration
   @ImportResource("spring-activemq-test.xml")
   @Import({ CoreMessagingConfiguration.class, ModelJPAConfiguration.class,
         CoreQuartzConfiguration.class })
   @ComponentScan(basePackageClasses={MftCoreAPI.class, MftScheduler.class, MftQueues.class, TransferExecutor.class})
   @PropertySource("classpath:core-test.properties")
   public static class TestConf {

   }

   @Autowired
   private MftCoreAPI mftCoreAPI;
   
   @Rule
   public TemporaryFolder folder = new TemporaryFolder();   
   
   @Value("${mft.storage.root}")
   private String rootDir;
   
   @Value("${mft.jms.data.dir}")
   private String jmsDataDir;
   
   private void deleteDirContents(String dirname) throws IOException {
      File dir = new File(dirname);
      if (dir.exists()) {
         FileUtils.deleteDirectory(dir);
      }
      dir.mkdirs();
   }
   
   @Before
   public void init() throws IOException {
      deleteDirContents(rootDir);
   }
   
   @After
   public void cleanup() throws IOException {
      deleteDirContents(rootDir);
      deleteDirContents(jmsDataDir);
   }

   @Test
   public void upAndRunning() {
      try {
         mftCoreAPI.bootstrapMft();

         Transfer transfer = new Transfer.Builder("testtransfer")
               // .withCronSchedule("0/20 * * * * ?")
               .startingInSeconds(5)
               // .fromNamedSource("MY_SENDER", "local:///tmp/sourcexxx")
               .fromNamedSource("MY_SCP_HOST",
                     "scp://scptest@localhost:22/home/scptest/send?password=scptest")
               .toTargets("local:///tmp/target1")
               // .toTargets("scp://scptest@localhost:22/home/scptest/receive?password=scptest")
               .files("*")
               .addProcessor(ProcessingType.COMPRESS, "destination",
                     "foo.tar.gz").usingReceivePolicies(
               // TransferReceivePolicies.LOCKSTRATEGY_PG_LEGACY,
                     TransferReceivePolicies.RETRY_ALLOWED)
               .usingSendPolicies(TransferSendPolicies.RETRY_ALLOWED).build();
         mftCoreAPI.saveAndScheduleTransfer(transfer);
         
         Thread.sleep(1000*10);
      } catch (BeansException | URISyntaxException | MftCoreAPIException e) {
         e.printStackTrace();
      } catch (InterruptedException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
