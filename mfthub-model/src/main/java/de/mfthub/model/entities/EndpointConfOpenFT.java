package de.mfthub.model.entities;

import javax.persistence.Entity;

@Entity
public class EndpointConfOpenFT extends EndpointConfWithAddressAndCredentials {
   protected String appendCommand;
   
   public EndpointConfOpenFT() {
      
   }

   public String getAppendCommand() {
      return appendCommand;
   }

   public void setAppendCommand(String appendCommand) {
      this.appendCommand = appendCommand;
   }
   
   
}
