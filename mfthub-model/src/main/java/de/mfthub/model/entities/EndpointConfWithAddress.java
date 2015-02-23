package de.mfthub.model.entities;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class EndpointConfWithAddress extends EndpointConfiguration {
   protected String dnsName;
   protected String ipAddressIPv4;
   protected String ipAddressIPv6;
   protected Integer port;
   
   
   public String getDnsName() {
      return dnsName;
   }
   public void setDnsName(String dnsName) {
      this.dnsName = dnsName;
   }
   public String getIpAddressIPv4() {
      return ipAddressIPv4;
   }
   public void setIpAddressIPv4(String ipAddressIPv4) {
      this.ipAddressIPv4 = ipAddressIPv4;
   }
   public String getIpAddressIPv6() {
      return ipAddressIPv6;
   }
   public void setIpAddressIPv6(String ipAddressIPv6) {
      this.ipAddressIPv6 = ipAddressIPv6;
   }
   public Integer getPort() {
      return port;
   }
   public void setPort(Integer port) {
      this.port = port;
   }

   
   
}
