package de.mfthub.model.entities;

import javax.persistence.Entity;

@Entity
public class EndpointConfLocalCp extends EndpointConfiguration {

   @Override
   public String toString() {
      return "EndpointConfLocalCp [id=" + id + ", directory=" + directory + "]";
   }
   
}
