package de.mfthub.transfer.exception;

import de.mfthub.model.entities.enums.ErrorCode;

@SuppressWarnings("serial")
public class TransmissionException extends Exception {
   
   private ErrorCode errorCode;

   public TransmissionException(ErrorCode errorCode) {
      super();
      this.errorCode = errorCode;
   }

   public TransmissionException(ErrorCode errorCode, String message, Throwable cause) {
      super(message, cause);
      this.errorCode = errorCode;
   }

   public TransmissionException(ErrorCode errorCode, String message) {
      super(message);
      this.errorCode = errorCode;
   }

   public TransmissionException(ErrorCode errorCode, Throwable cause) {
      super(cause);
      this.errorCode = errorCode;
   }

   public ErrorCode getErrorCode() {
      return errorCode;
   }

}
