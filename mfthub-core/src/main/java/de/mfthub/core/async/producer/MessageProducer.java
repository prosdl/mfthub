package de.mfthub.core.async.producer;

public interface MessageProducer {

   void queueDeliveryForInboundMessage(String deliveryUuid, String transferUuid);

}
