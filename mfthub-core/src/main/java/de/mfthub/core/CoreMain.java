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
               .withCronSchedule("0/10 * * * * ?")
               .fromNamedSource("MY_SENDER", "local:///tmp/source")
               .toTargets("local:///tmp/target1", "local:///tmp/target2")
               .files("bar/**/*.pdf")
               .addProcessor(ProcessingType.COMPRESS, "destination",
                     "foo.tar.gz")
               .usingReceivePolicies(
                     TransferReceivePolicies.LOCKSTRATEGY_PG_LEGACY).build();
         mftCoreAPI.saveAndScheduleTransfer(transfer);
      } catch (BeansException | URISyntaxException | MftCoreAPIException e) {
         e.printStackTrace();
         SpringApplication.exit(ctx);
      }

   }
}
