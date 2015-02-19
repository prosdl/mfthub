package de.mfthub.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import de.mfthub.rest.json.CustomMapper;
import de.mfthub.rest.resources.TenantResource;

@Configuration
public class MftHubRestConfig extends ResourceConfig{
   public MftHubRestConfig() {
      register(JacksonFeature.class);
      register(TenantResource.class);
      register(CustomMapper.class);
   }
}
