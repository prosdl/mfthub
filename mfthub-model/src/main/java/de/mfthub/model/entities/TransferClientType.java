package de.mfthub.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Deprecated
public class TransferClientType {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private Long id;
   private String implementingClass;
   
   public TransferClientType() {
      
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getImplementingClass() {
      return implementingClass;
   }

   public void setImplementingClass(String implementingClass) {
      this.implementingClass = implementingClass;
   }
   
   
}
