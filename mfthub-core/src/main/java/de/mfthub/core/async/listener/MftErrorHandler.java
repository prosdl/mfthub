package de.mfthub.core.async.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class MftErrorHandler implements ErrorHandler {
   private static Logger LOG = LoggerFactory.getLogger(MftErrorHandler.class);

   @Override
   public void handleError(Throwable throwable) {
      LOG.error("JMS errorhandler: {}", throwable.getMessage(), throwable);
   }

}
