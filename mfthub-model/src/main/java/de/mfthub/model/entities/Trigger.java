package de.mfthub.model.entities;

import java.util.Date;

import javax.persistence.Embeddable;

@Embeddable
public class Trigger {
   private String cronExpresion;
   private Date startAt;
   private Date endAt;

   public String getCronExpresion() {
      return cronExpresion;
   }

   public void setCronExpresion(String cronExpresion) {
      this.cronExpresion = cronExpresion;
   }

   public Date getStartAt() {
      return startAt;
   }

   public void setStartAt(Date startAt) {
      this.startAt = startAt;
   }

   public Date getEndAt() {
      return endAt;
   }

   public void setEndAt(Date endAt) {
      this.endAt = endAt;
   }

}
