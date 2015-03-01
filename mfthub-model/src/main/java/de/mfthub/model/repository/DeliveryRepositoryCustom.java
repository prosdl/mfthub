package de.mfthub.model.repository;

import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.enums.DeliveryState;

@Repository
public interface DeliveryRepositoryCustom {
   public void updateDeliveryState(Delivery delivery,
         DeliveryState deliveryState, String action, String details);

   void updateDeliveryState(String deliveryUuid, DeliveryState deliveryState,
         String action, String details);
}