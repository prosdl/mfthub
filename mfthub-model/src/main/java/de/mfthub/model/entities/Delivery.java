package de.mfthub.model.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.GenericGenerator;

import de.mfthub.model.entities.enums.DeliveryState;

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
   
   @Enumerated(EnumType.STRING)
   private DeliveryState state;
   
   @OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
   @OrderBy("timeOfChange ASC")
   private List<DeliveryStateChangeEvent> stateChanges = new ArrayList<>();

   public Delivery() {
      initiated = new Date();
      setState(DeliveryState.INITIATED);
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

   public DeliveryState getState() {
      return state;
   }

   public void setState(DeliveryState state) {
      this.state = state;
   }

   public List<DeliveryStateChangeEvent> getStateChanges() {
      return stateChanges;
   }

   public void setStateChanges(List<DeliveryStateChangeEvent> stateChanges) {
      this.stateChanges = stateChanges;
   }

}
