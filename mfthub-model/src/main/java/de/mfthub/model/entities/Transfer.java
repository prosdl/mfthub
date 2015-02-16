package de.mfthub.model.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import de.mfthub.model.entities.enums.TransferPolicies;

@Entity
public class Transfer {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private Long id;
   
   @ManyToOne(optional=false)
   private Endpoint source;
   
   @ManyToMany
   private List<Endpoint> targets;
   
   @ElementCollection(targetClass=TransferPolicies.class)
   @Enumerated(EnumType.STRING)
   private Set<TransferPolicies> transferPolicies;
   
   @ManyToOne(optional=false)
   private AdministrativeApplication administrativeApplication;
   
   @ManyToOne(optional=false)
   private Tenant tenant;
   
   @OneToMany
   private List<Transformation> transformations;
   
   public Transfer() {
      
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Endpoint getSource() {
      return source;
   }

   public void setSource(Endpoint source) {
      this.source = source;
   }

   public List<Endpoint> getTargets() {
      return targets;
   }

   public void setTargets(List<Endpoint> targets) {
      this.targets = targets;
   }

   public Set<TransferPolicies> getTransferPolicies() {
      return transferPolicies;
   }

   public void setTransferPolicies(Set<TransferPolicies> transferPolicies) {
      this.transferPolicies = transferPolicies;
   }

   public AdministrativeApplication getAdministrativeApplication() {
      return administrativeApplication;
   }

   public void setAdministrativeApplication(
         AdministrativeApplication administrativeApplication) {
      this.administrativeApplication = administrativeApplication;
   }

   public Tenant getTenant() {
      return tenant;
   }

   public void setTenant(Tenant tenant) {
      this.tenant = tenant;
   }

   public List<Transformation> getTransformations() {
      return transformations;
   }

   public void setTransformations(List<Transformation> transformations) {
      this.transformations = transformations;
   }
   
   
}
