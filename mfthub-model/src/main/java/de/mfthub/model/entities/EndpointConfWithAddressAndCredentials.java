package de.mfthub.model.entities;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class EndpointConfWithAddressAndCredentials extends EndpointConfWithAddress {
   protected String userid;
   protected String password;
   protected String x509Fingerprint;
   
   @Lob
   private String x509PEM;
   
   
   public String getUserid() {
      return userid;
   }
   public void setUserid(String userid) {
      this.userid = userid;
   }
   public String getPassword() {
      return password;
   }
   public void setPassword(String password) {
      this.password = password;
   }
   public String getX509Fingerprint() {
      return x509Fingerprint;
   }
   public void setX509Fingerprint(String x509Fingerprint) {
      this.x509Fingerprint = x509Fingerprint;
   }
   public String getX509PEM() {
      return x509PEM;
   }
   public void setX509PEM(String x509pem) {
      x509PEM = x509pem;
   }
   
   
}
