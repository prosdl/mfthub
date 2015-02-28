package de.mfthub.model.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

public class JSON {
   
   public static String toJson(Object o) {
      ObjectMapper om = new ObjectMapper();
      om.setSerializationInclusion(Inclusion.NON_NULL);
      try {
         return om.writerWithDefaultPrettyPrinter().writeValueAsString(o);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
