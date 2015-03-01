package de.mfthub.core.async.listener;


public interface OutboundListener {
   public void send(String deliveryUuid);
}
