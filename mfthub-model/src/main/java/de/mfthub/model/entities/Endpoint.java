package de.mfthub.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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


   @ManyToOne(optional=false)
   private TransferClientType transferClientType;

   @OneToOne(optional=false)
   private EndpointConfiguration endpointConfiguration;

   public Endpoint() {

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

   public TransferClientType getTransferClientType() {
      return transferClientType;
   }

   public void setTransferClientType(TransferClientType transferClientType) {
      this.transferClientType = transferClientType;
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

}
