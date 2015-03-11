package de.mfthub.core;

import java.net.URISyntaxException;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import de.mfthub.core.api.MftCoreAPI;
import de.mfthub.core.api.MftCoreAPIException;
import de.mfthub.core.conf.CoreMessagingConfiguration;
import de.mfthub.core.conf.CoreQuartzConfiguration;
import de.mfthub.model.conf.ModelJPAConfiguration;
import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.enums.ProcessingType;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;

@SpringBootApplication()
@Import({ CoreMessagingConfiguration.class, ModelJPAConfiguration.class,
      CoreQuartzConfiguration.class })
public class CoreMain {
   public static void main(String[] args) {
      ApplicationContext ctx = SpringApplication.run(CoreMain.class, args);

      try {
         MftCoreAPI mftCoreAPI = ctx.getBean(MftCoreAPI.class);
         mftCoreAPI.bootstrapMft();

         Transfer transfer = new Transfer.Builder("testtransfer")
//               .withCronSchedule("0/20 * * * * ?")
               .startingInSeconds(10)
//               .fromNamedSource("MY_SENDER", "local:///tmp/sourcexxx")
               .fromNamedSource("MY_SCP_HOST", "scp://scptest@localhost:22/home/scptest/send?password=scptest")
               .toTargets("local:///tmp/target1")
//               .toTargets("scp://scptest@localhost:22/home/scptest/receive?password=scptest")
               .files("*")
               .addProcessor(ProcessingType.COMPRESS, "archiveName",
                     "my_files.tar.gz")
               .usingReceivePolicies(
//                     TransferReceivePolicies.LOCKSTRATEGY_PG_LEGACY, 
                     TransferReceivePolicies.RETRY_ALLOWED)
               .usingSendPolicies(TransferSendPolicies.RETRY_ALLOWED)
               .build();
         mftCoreAPI.saveAndScheduleTransfer(transfer);
      } catch (BeansException | URISyntaxException | MftCoreAPIException e) {
         e.printStackTrace();
         SpringApplication.exit(ctx);
      }

   }
}
