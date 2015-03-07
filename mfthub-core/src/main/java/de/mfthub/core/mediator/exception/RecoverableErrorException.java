package de.mfthub.core.mediator.exception;

@SuppressWarnings("serial")
public class RecoverableErrorException extends TransferExcecutionException {

   public RecoverableErrorException() {
      super();
   }

   public RecoverableErrorException(String message, Throwable cause) {
      super(message, cause);
   }

   public RecoverableErrorException(String message) {
      super(message);
   }

   public RecoverableErrorException(Throwable cause) {
      super(cause);
   }

}
