package de.mfthub.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TransformationParam {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private Long id;
   
   @ManyToOne(optional=false)
   private TransformationType type;
   private String name;
   
   public TransformationParam() {
      
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public TransformationType getType() {
      return type;
   }

   public void setType(TransformationType type) {
      this.type = type;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
   
   
}
