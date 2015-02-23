package de.mfthub.model.entities;

import javax.persistence.Entity;

@Entity
public class EndpointConfScp extends EndpointConfWithAddressAndCredentials {

   @Override
   public String toString() {
      return "EndpointConfScp [userid=" + userid + ", password=" + password
            + ", x509Fingerprint=" + x509Fingerprint + ", dnsName=" + dnsName
            + ", ipAddressIPv4=" + ipAddressIPv4 + ", ipAddressIPv6="
            + ipAddressIPv6 + ", port=" + port + ", id=" + id + ", directory="
            + directory + "]";
   }



}
