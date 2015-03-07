package de.mfthub.model.entities.enums;

public enum DeliveryState {
   INITIATED,
   FILES_INBOUND,
   PROCESSING_READY,
   PROCESSING_DONE,
   OUTBOUND_READY,
   FILES_SEND,
   SUCCESS,
   ERROR_TRYING_RECOVERY,
   ERROR_NO_AUTO_RECOVERY
}
