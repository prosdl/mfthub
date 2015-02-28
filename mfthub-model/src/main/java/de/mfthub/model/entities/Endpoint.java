package de.mfthub.model.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import de.mfthub.model.entities.enums.Protocol;

@Entity
public class Endpoint {
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @NotNull
   @Pattern(regexp="[a-zA-Z0-9_\\-\\.]+")
   @Column(unique=true)
   private String endpointKey;
   
   private String description;
   
   private String operatingSystem;
   
   @NotNull
   private boolean active;

   @Enumerated(EnumType.STRING)
   private Protocol protocol;

   @OneToOne(optional=false, cascade={CascadeType.PERSIST, CascadeType.MERGE})
   private EndpointConfiguration endpointConfiguration;

   public Endpoint() {

   }
   
   

   @Override
   public String toString() {
      return "Endpoint [id=" + id + ", endpointKey=" + endpointKey
            + ", description=" + description + ", operatingSystem="
            + operatingSystem + ", active=" + active + ", protocol=" + protocol
            + ", endpointConfiguration=" + endpointConfiguration + "]";
   }



   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public EndpointConfiguration getEndpointConfiguration() {
      return endpointConfiguration;
   }

   public void setEndpointConfiguration(
         EndpointConfiguration endpointConfiguration) {
      this.endpointConfiguration = endpointConfiguration;
   }

   public String getEndpointKey() {
      return endpointKey;
   }

   public void setEndpointKey(String endpointKey) {
      this.endpointKey = endpointKey;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getOperatingSystem() {
      return operatingSystem;
   }

   public void setOperatingSystem(String operatingSystem) {
      this.operatingSystem = operatingSystem;
   }

   public boolean isActive() {
      return active;
   }

   public void setActive(boolean active) {
      this.active = active;
   }

   public Protocol getProtocol() {
      return protocol;
   }

   public void setProtocol(Protocol protocol) {
      this.protocol = protocol;
   }

   
}
