package de.mfthub.transfer.exception;

@SuppressWarnings("serial")
public class TransmissionMisconfigurationException extends Exception {

   public TransmissionMisconfigurationException() {
      super();
   }

   public TransmissionMisconfigurationException(String message, Throwable cause) {
      super(message, cause);
   }

   public TransmissionMisconfigurationException(String message) {
      super(message);
   }

   public TransmissionMisconfigurationException(Throwable cause) {
      super(cause);
   }

}
