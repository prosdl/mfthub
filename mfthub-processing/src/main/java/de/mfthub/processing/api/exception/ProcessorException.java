package de.mfthub.processing.api.exception;

@SuppressWarnings("serial")
public class ProcessorException extends Exception {

   public ProcessorException() {
      super();
   }

   public ProcessorException(String message, Throwable cause) {
      super(message, cause);
   }

   public ProcessorException(String message) {
      super(message);
   }

   public ProcessorException(Throwable cause) {
      super(cause);
   }

}
