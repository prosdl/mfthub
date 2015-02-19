package de.mfthub.rest.resources;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import jersey.repackaged.com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.mfthub.model.entities.Tenant;
import de.mfthub.model.repository.TenantRepository;

@Component
@Path("/tenant")
@Produces({ MediaType.APPLICATION_JSON })
public class TenantResource {
   private static Logger LOG = LoggerFactory.getLogger(TenantResource.class);
   
   @Autowired
   private TenantRepository tenantRepository;
   
   @GET
   @Path("/{id}")
   public List<Tenant> get(@PathParam("id") Long id) {
      LOG.info("get: " + id);
      LOG.info("ten: " + tenantRepository);
      Tenant foo = new Tenant();
      foo.setId(id);
      foo.setName("foobar" + UUID.randomUUID());
      
      tenantRepository.save(foo);
      
      return Lists.newArrayList(tenantRepository.findAll());
   }
}
