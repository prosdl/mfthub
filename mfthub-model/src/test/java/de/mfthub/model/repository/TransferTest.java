package de.mfthub.model.repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.transaction.Transactional;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import com.google.common.collect.Lists;

import de.mfthub.model.entities.AdministrativeApplication;
import de.mfthub.model.entities.Tenant;
import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.util.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringApplicationConfiguration(classes = SimpleTestConfigurationJPA.class)
@TransactionConfiguration(defaultRollback = true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class TransferTest {

   @Autowired
   private TransferRepository transferRepository;
   @Autowired
   private AdministrativeApplicationRepository administrativeApplicationRepository;
   @Autowired
   private TenantRepository tenantRepository;

   @Test
   public void saveOrUpdateTransfer() throws URISyntaxException, JsonGenerationException, JsonMappingException, IOException {
      
      Transfer transfer = new Transfer.Builder("testtransfer")
            .withCronSchedule("0/10 * * * * ?")
            .fromNamedSource("MY_SENDER", "local:///tmp/source")
            .toTargets("local:///tmp/foo")
            .files("bar/**/*.pdf")
            .usingReceivePolicies(
                  TransferReceivePolicies.LOCKSTRATEGY_PG_LEGACY).build();

      Transfer mergedTransfer = transferRepository.saveOrUpdate(transfer);
      
      System.out.println(JSON.toJson(mergedTransfer));

      assertThat(mergedTransfer.getUuid(), notNullValue());
      assertThat(mergedTransfer.getSource().getId(), notNullValue());
      assertThat(mergedTransfer.getTargets(), notNullValue());
      assertThat(mergedTransfer.getTargets(), hasSize(1));
      assertThat(mergedTransfer.getTargets().get(0).getId(), notNullValue());
      assertThat(mergedTransfer.getFileSelector().getId(), notNullValue());
      assertThat(mergedTransfer.getAdministrativeApplication().getId(), notNullValue());
      assertThat(mergedTransfer.getAdministrativeApplication(), equalTo(AdministrativeApplication.INTERNAL_ADMIN_APP));
      assertThat(mergedTransfer.getTenant().getId(), notNullValue());
      assertThat(mergedTransfer.getTenant(), equalTo(Tenant.INTERNAL_TENANT));
      
      transfer.setName("test2");
      Transfer mergedTransfer2 = transferRepository.saveOrUpdate(transfer);
      System.out.println(JSON.toJson(mergedTransfer2));

      assertThat(mergedTransfer.getUuid(), not(equalTo(mergedTransfer2.getUuid())));
      List<Tenant> allTenants = Lists.newArrayList(tenantRepository.findAll());
      assertThat(allTenants, hasSize(1));
   }

}
