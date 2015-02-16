package de.mfthub.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Endpoint {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   private String hostName;
   private Integer port;
   private String userid;

   public Endpoint() {

   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getHostName() {
      return hostName;
   }

   public void setHostName(String hostName) {
      this.hostName = hostName;
   }

   public Integer getPort() {
      return port;
   }

   public void setPort(Integer port) {
      this.port = port;
   }

   public String getUserid() {
      return userid;
   }

   public void setUserid(String userid) {
      this.userid = userid;
   }

}
