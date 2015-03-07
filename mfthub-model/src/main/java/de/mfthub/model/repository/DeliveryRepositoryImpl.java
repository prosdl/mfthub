package de.mfthub.model.repository;

import java.util.Date;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.DeliveryStateChangeEvent;
import de.mfthub.model.entities.enums.DeliveryState;
import de.mfthub.model.util.JSON;

@Repository
public class DeliveryRepositoryImpl implements DeliveryRepositoryCustom {
   private static Logger LOG = LoggerFactory.getLogger(DeliveryRepositoryImpl.class);
   
   @Autowired
   private EntityManager entityManager;
   
   @Override
   public void updateDeliveryState(Delivery delivery,
         DeliveryState deliveryState, String action, String details) {
      LOG.info("Updating delivery {} to {} ", delivery.getUuid(), deliveryState);
      Delivery persistedDelivery = entityManager.find(Delivery.class, delivery.getUuid());
      
      persistedDelivery.setState(deliveryState);
      DeliveryStateChangeEvent changeEvent = new DeliveryStateChangeEvent();
      changeEvent.setAction(action);
      changeEvent.setChangedTo(deliveryState);
      changeEvent.setDetails(details);
      changeEvent.setTimeOfChange(new Date());
      changeEvent.setDelivery(persistedDelivery);
      entityManager.persist(changeEvent);
      
      persistedDelivery.getStateChanges().add(changeEvent);
      entityManager.persist(persistedDelivery);
      entityManager.flush();
      LOG.info("Details\n{}",JSON.toJson(persistedDelivery));
   }

   @Override
   public void updateDeliveryState(String deliveryUuid,
         DeliveryState deliveryState, String action, String details) {
      Delivery delivery = entityManager.find(Delivery.class, deliveryUuid);
      if (delivery == null) {
         throw new IllegalStateException("No delivery found for " + deliveryUuid);
      }
      updateDeliveryState(delivery, deliveryState, action, details);
   }

}