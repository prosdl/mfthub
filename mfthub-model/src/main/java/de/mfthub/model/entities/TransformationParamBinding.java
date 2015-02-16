package de.mfthub.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class TransformationParamBinding {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private Long id;
   
   @ManyToOne(optional=false)
   private TransformationParam param;
   private String value;
   
   public TransformationParamBinding() {
      
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public TransformationParam getParam() {
      return param;
   }

   public void setParam(TransformationParam param) {
      this.param = param;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }
   
   
}
