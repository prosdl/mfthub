package de.mfthub.model.entities.enums;

import java.net.URISyntaxException;

@SuppressWarnings("serial")
public class MftUriSyntaxException extends URISyntaxException {

   public MftUriSyntaxException(String input, String reason) {
      super(input, reason);
   }
}
