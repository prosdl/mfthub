package de.mfthub.core.api;

@SuppressWarnings("serial")
public class MftCoreAPIException extends Exception {

   public MftCoreAPIException() {
      super();
   }

   public MftCoreAPIException(String message, Throwable cause) {
      super(message, cause);
   }

   public MftCoreAPIException(String message) {
      super(message);
   }

   public MftCoreAPIException(Throwable cause) {
      super(cause);
   }

}
