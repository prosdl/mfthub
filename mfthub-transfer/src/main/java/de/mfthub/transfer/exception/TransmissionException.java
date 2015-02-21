package de.mfthub.transfer.exception;

@SuppressWarnings("serial")
public class TransmissionException extends Exception {

   public TransmissionException() {
      super();
   }

   public TransmissionException(String message, Throwable cause) {
      super(message, cause);
   }

   public TransmissionException(String message) {
      super(message);
   }

   public TransmissionException(Throwable cause) {
      super(cause);
   }

}
