package de.mfthub.model;

import javax.transaction.Transactional;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;

import de.mfthub.model.entities.Tenant;
import de.mfthub.model.repository.TenantRepository;

@EnableAutoConfiguration
@ContextConfiguration(classes = SimpleTestConfigurationJPA.class, loader = AnnotationConfigContextLoader.class)
@TransactionConfiguration(defaultRollback = true)
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleTest {

   @Autowired
   private TenantRepository tenantRepository;

   @Test
   public void test() {
      Tenant t = new Tenant();
      t.setName("test-tenant");
      tenantRepository.save(t);

      Tenant newTenant = tenantRepository.findByName("test-tenant");
      Assert.assertNotNull(newTenant);
      Assert.assertNotNull(newTenant.getId());
   }
}
