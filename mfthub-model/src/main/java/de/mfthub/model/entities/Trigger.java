package de.mfthub.model.entities;

import javax.persistence.Embeddable;

@Embeddable
public class Trigger {
   private String cronExpresion;

   public String getCronExpresion() {
      return cronExpresion;
   }

   public void setCronExpresion(String cronExpresion) {
      this.cronExpresion = cronExpresion;
   }
   
   
   
}
