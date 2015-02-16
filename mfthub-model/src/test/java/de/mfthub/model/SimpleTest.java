package de.mfthub.model;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import de.mfthub.model.entities.Tenant;
import de.mfthub.model.repository.ITenantDAO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-model-embedded.xml" }) 
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class SimpleTest {
   
   @Autowired
   private ITenantDAO tenantDAO;

   @Test
   public void test() {
      Tenant t = new Tenant();
      t.setName("test-tenant");
      tenantDAO.saveOrUpdate(t);
   }
}
