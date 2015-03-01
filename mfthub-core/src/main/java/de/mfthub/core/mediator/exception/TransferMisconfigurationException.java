package de.mfthub.core.mediator.exception;

@SuppressWarnings("serial")
public class TransferMisconfigurationException extends Exception {

   public TransferMisconfigurationException() {
      super();
   }

   public TransferMisconfigurationException(String message, Throwable cause) {
      super(message, cause);
   }

   public TransferMisconfigurationException(String message) {
      super(message);
   }

   public TransferMisconfigurationException(Throwable cause) {
      super(cause);
   }
 
}
