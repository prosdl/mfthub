package de.mfthub.model.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Delivery {
   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   private String uuid;

   @ManyToOne(optional = false)
   private Transfer transfer;

   private Date initiated;
   private Date finished;
   private String errorDetails;

   public Delivery() {

   }

   public String getUuid() {
      return uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public Transfer getTransfer() {
      return transfer;
   }

   public void setTransfer(Transfer transfer) {
      this.transfer = transfer;
   }

   public Date getInitiated() {
      return initiated;
   }

   public void setInitiated(Date initiated) {
      this.initiated = initiated;
   }

   public Date getFinished() {
      return finished;
   }

   public void setFinished(Date finished) {
      this.finished = finished;
   }

   public String getErrorDetails() {
      return errorDetails;
   }

   public void setErrorDetails(String errorDetails) {
      this.errorDetails = errorDetails;
   }

}
