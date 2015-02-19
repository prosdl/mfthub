package de.mfthub.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

@Entity
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;
   private String name;
   private String hashedClientPassword;

   public User() {

   }

   public User(String clientId, String clearPassword) {
      this.name = clientId;
      ShaPasswordEncoder passowrdEncoder = new ShaPasswordEncoder(256);
      passowrdEncoder.setEncodeHashAsBase64(true);
      this.hashedClientPassword = passowrdEncoder.encodePassword(clearPassword,
            null);
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getHashedClientPassword() {
      return hashedClientPassword;
   }

   public void setHashedClientPassword(String hashedClientPassword) {
      this.hashedClientPassword = hashedClientPassword;
   }

}
