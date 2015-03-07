package de.mfthub.core.mediator.exception;

@SuppressWarnings("serial")
public class NonRecoverableErrorException extends TransferExcecutionException {

   public NonRecoverableErrorException() {
      super();
   }

   public NonRecoverableErrorException(String message, Throwable cause) {
      super(message, cause);
   }

   public NonRecoverableErrorException(String message) {
      super(message);
   }

   public NonRecoverableErrorException(Throwable cause) {
      super(cause);
   }

}
