package de.mfthub.core;

import java.net.URISyntaxException;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import de.mfthub.core.conf.CoreConfiguration;
import de.mfthub.core.mediator.TransferExecutor;
import de.mfthub.model.conf.ModelJPAConfiguration;
import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.transfer.exception.TransmissionException;
import de.mfthub.transfer.exception.TransmissionMisconfigurationException;

@SpringBootApplication()
@Import({ CoreConfiguration.class, ModelJPAConfiguration.class })
public class CoreMain {
   public static void main(String[] args) {
      ApplicationContext ctx = SpringApplication.run(CoreMain.class, args);

      try {
         TransferExecutor te = ctx.getBean(TransferExecutor.class);
         Transfer transfer = new Transfer.Builder("testtransfer")
               .withRandamUUID()
               .fromNamedSource("MY_SENDER", "local:///tmp/source")
               .toTargets("local:///tmp/foo")
               .files("bar/**/*.pdf")
               .usingReceivePolicies(
                     TransferReceivePolicies.LOCKSTRATEGY_PG_LEGACY).build();
         te.receive(transfer);
      } catch (BeansException | URISyntaxException | TransmissionException
            | TransmissionMisconfigurationException e) {
         e.printStackTrace();
      }

      SpringApplication.exit(ctx);
   }
}
