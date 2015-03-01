package de.mfthub.model.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.mfthub.model.entities.enums.DeliveryState;

@Entity
public class DeliveryStateChangeEvent {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @JsonIgnore
   @ManyToOne(optional = false)
   private Delivery delivery;

   @NotNull
   @Enumerated(EnumType.STRING)
   private DeliveryState changedTo;
   
   private Date timeOfChange;
   private String action;
   
   @Lob
   private String details;

   public DeliveryStateChangeEvent() {

   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Delivery getDelivery() {
      return delivery;
   }

   public void setDelivery(Delivery delivery) {
      this.delivery = delivery;
   }

   public DeliveryState getChangedTo() {
      return changedTo;
   }

   public void setChangedTo(DeliveryState changedTo) {
      this.changedTo = changedTo;
   }

   public Date getTimeOfChange() {
      return timeOfChange;
   }

   public void setTimeOfChange(Date timeOfChange) {
      this.timeOfChange = timeOfChange;
   }

   public String getDetails() {
      return details;
   }

   public void setDetails(String details) {
      this.details = details;
   }

   public String getAction() {
      return action;
   }

   public void setAction(String action) {
      this.action = action;
   }

}
