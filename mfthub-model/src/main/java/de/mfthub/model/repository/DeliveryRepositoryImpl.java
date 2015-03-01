package de.mfthub.model.repository;

import java.util.Date;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.DeliveryStateChangeEvent;
import de.mfthub.model.entities.enums.DeliveryState;

@Repository
public class DeliveryRepositoryImpl implements DeliveryRepositoryCustom {

   
   @Autowired
   private EntityManager entityManager;
   
   @Override
   public void updateDeliveryState(Delivery delivery,
         DeliveryState deliveryState, String action, String details) {
      Delivery persistedDelivery = entityManager.find(Delivery.class, delivery.getUuid());
      
      DeliveryStateChangeEvent changeEvent = new DeliveryStateChangeEvent();
      changeEvent.setAction(action);
      changeEvent.setChangedTo(deliveryState);
      changeEvent.setDetails(details);
      changeEvent.setTimeOfChange(new Date());
      changeEvent.setDelivery(persistedDelivery);
      entityManager.persist(changeEvent);
      
      persistedDelivery.getStateChanges().add(changeEvent);
   }

   @Override
   public void updateDeliveryState(String deliveryUuid,
         DeliveryState deliveryState, String action, String details) {
      Delivery delivery = entityManager.find(Delivery.class, deliveryUuid);
      updateDeliveryState(delivery, deliveryState, action, details);
   }

}