package de.mfthub.rest.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import de.mfthub.model.entities.User;

public class MftClientDetailsService  implements ClientDetailsService{
   private static Logger logger = LoggerFactory.getLogger(MftClientDetailsService.class);

   
   private Set<GrantedAuthority> getAuth() {
      HashSet<GrantedAuthority> set = new HashSet<>();
      set.add(new GrantedAuthority() {
         
         @Override
         public String getAuthority() {
            return "ROLE_USER";
         }
      });
      return set;
   }

   @Override
   public ClientDetails loadClientByClientId(String clientId)
         throws ClientRegistrationException {
      // FIXME
      User oauthUser = new User(clientId, clientId);
      BaseClientDetails clientDetails = new BaseClientDetails();
      clientDetails.setAccessTokenValiditySeconds(3600*8);
      clientDetails.setAuthorities(getAuth());
      clientDetails.setClientId(clientId);
      clientDetails.setClientSecret(oauthUser.getHashedClientPassword());
      clientDetails.setAuthorizedGrantTypes(Arrays.asList("client_credentials"));;
      return clientDetails;
   }

}