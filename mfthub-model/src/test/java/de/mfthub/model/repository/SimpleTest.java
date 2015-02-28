package de.mfthub.model.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import de.mfthub.model.entities.EndpointConfScp;
import de.mfthub.model.entities.EndpointConfiguration;
import de.mfthub.model.entities.Tenant;

@SpringApplicationConfiguration(classes=SimpleTestConfigurationJPA.class)
@TransactionConfiguration(defaultRollback = true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleTest {

   @Autowired
   private TenantRepository tenantRepository;
   
   @Autowired
   private EndpointConfigurationRepository endpointConfigurationRepository;

   @Test
   public void insertAndFindTenant() {
      Tenant t = new Tenant();
      t.setName("test-tenant");
      tenantRepository.save(t);

      Tenant newTenant = tenantRepository.findByName("test-tenant");
      assertNotNull(newTenant);
      assertNotNull(newTenant.getId());
   }
   
   @Test
   public void createEndpointConfiguration() {
      EndpointConfScp e = new EndpointConfScp();
      
      e.setDirectory("/tmp");
      e.setDnsName("foo.bar.de");
      
      endpointConfigurationRepository.save(e);
      
      assertNotNull(e.getId());
      
      EndpointConfiguration ec = endpointConfigurationRepository.findOne(e.getId());
      
      System.out.println(ec.getDirectory());
      
      assertTrue(ec instanceof EndpointConfScp);
      
      System.out.println(((EndpointConfScp)ec).getDnsName());
   }
}
