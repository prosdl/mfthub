package de.mfthub.core.async.producer;

import de.mfthub.model.entities.enums.DeliveryState;

public interface MessageProducer {

   void queueDeliveryForInboundMessage(String deliveryUuid, String transferUuid);

   void redeliverMessage(String targetQueue, String deliveryUuid, String nextState);

   void sendToRedeliveryQueue(String deliveryUuid, DeliveryState state,
         String toQueue);

}
