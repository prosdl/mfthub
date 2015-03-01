package de.mfthub.core.mediator.exception;

@SuppressWarnings("serial")
public class TransferExcecutionException extends Exception {

   public TransferExcecutionException() {
      super();
   }

   public TransferExcecutionException(String message, Throwable cause) {
      super(message, cause);
   }

   public TransferExcecutionException(String message) {
      super(message);
   }

   public TransferExcecutionException(Throwable cause) {
      super(cause);
   }
}
