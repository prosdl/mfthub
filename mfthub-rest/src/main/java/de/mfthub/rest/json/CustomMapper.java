package de.mfthub.rest.json;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
public class CustomMapper implements ContextResolver<ObjectMapper> {

   final ObjectMapper defaultObjectMapper;

   public CustomMapper() {
      defaultObjectMapper = createDefaultMapper();
   }

   @Override
   public ObjectMapper getContext(Class<?> type) {
      return defaultObjectMapper;
   }

   private static ObjectMapper createDefaultMapper() {
      final ObjectMapper result = new ObjectMapper();
      result.enable(SerializationFeature.INDENT_OUTPUT);
      return result;
   }

}