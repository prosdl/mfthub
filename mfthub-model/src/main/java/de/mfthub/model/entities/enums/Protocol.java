package de.mfthub.model.entities.enums;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.common.base.Splitter;

import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.EndpointConfLocalCp;
import de.mfthub.model.entities.EndpointConfScp;
import de.mfthub.model.entities.EndpointConfiguration;

public enum Protocol {
   SCP {
      @Override
      public EndpointConfiguration construct(URI uri) {
         Map<String,String> params = getQueryMap(uri);
         ObjectMapper om = new ObjectMapper();
         EndpointConfScp conf = om.convertValue(params, EndpointConfScp.class);
         conf.setDnsName(uri.getHost());
         conf.setPort(uri.getPort());
         conf.setUserid(uri.getUserInfo());
         conf.setDirectory(uri.getPath());
         return conf;
      }
   },
   LOCAL {
      @Override
      public EndpointConfiguration construct(URI uri) {
         EndpointConfLocalCp conf = new EndpointConfLocalCp();
         conf.setDirectory(uri.getPath());
         return conf;
      }
   };

   public abstract EndpointConfiguration construct(URI uri);
   
   private static Map<String, String> getQueryMap(URI uri) {
      String query = uri.getQuery();
      return Splitter.on('&').trimResults().withKeyValueSeparator('=').split(query);
   }

   private Protocol() {
   }

   /**
    * Constructs an Endpoint from a URI.
    * 
    * @param endpointName
    * @param uriString
    * @return
    * @throws URISyntaxException
    */
   public static Endpoint buildEndpointFromURI(String endpointName,
         String uriString) throws URISyntaxException {
      URI uri = new URI(uriString);
      if (uri.getScheme() == null) {
         throw new MftUriSyntaxException(uri.toString(),
               "<scheme> part required for mft URIs.");
      }
      Protocol protocol = null;
      try {
         protocol = valueOf(uri.getScheme().toUpperCase());
      } catch (RuntimeException re) {
         throw new MftUriSyntaxException(uri.toString(),
               "Couldn't create endpoint from URI.");
      }
      Endpoint endpoint = new Endpoint();
      endpoint.setActive(true);
      endpoint.setEndpointKey(endpointName);
      endpoint.setEndpointConfiguration(protocol.construct(uri));
      endpoint.setProtocol(protocol);
      return endpoint;
   }

   /**
    * Probably only useful for testing or CLI file transfers. The endpoint gets
    * a random name starting with "__ep__".
    * 
    * @param uriString
    * @return
    * @throws URISyntaxException
    */
   public static Endpoint buildEndpointFromURI(String uriString)
         throws URISyntaxException {
      return buildEndpointFromURI("__ep__"
            + new BigInteger(6 * 5, new SecureRandom()).toString(32), uriString);
   }

   public static void main(String[] args) throws URISyntaxException {
      System.out.println(Protocol.buildEndpointFromURI("local:///tmp/foo"));
      System.out.println(Protocol
            .buildEndpointFromURI("scp://peter@foobar.de:1234/tmp/foo"));
   }
}
