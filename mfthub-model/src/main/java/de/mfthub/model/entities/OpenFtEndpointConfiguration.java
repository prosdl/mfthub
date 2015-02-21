package de.mfthub.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//@Entity
public class OpenFtEndpointConfiguration extends EndpointConfiguration {
   
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private String appendCommand;
   
   public OpenFtEndpointConfiguration() {
      
   }

   public String getAppendCommand() {
      return appendCommand;
   }

   public void setAppendCommand(String appendCommand) {
      this.appendCommand = appendCommand;
   }
   
   
}
