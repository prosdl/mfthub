package de.mfthub.model.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

public class JSON {
   // ObjectMappers are thread safe, see: jackson faq
   private static ObjectMapper objectMapper = initObjectMapper();
   
   private static ObjectMapper initObjectMapper() {
      ObjectMapper om =  new ObjectMapper();
      om.setSerializationInclusion(Inclusion.NON_NULL);
      om.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
      return om;
   }
   
   public static String toJson(Object o) {
      try {
         return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
   }
}
