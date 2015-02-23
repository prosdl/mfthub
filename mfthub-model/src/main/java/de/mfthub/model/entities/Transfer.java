package de.mfthub.model.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;

@Entity
public class Transfer {
   @Id
   @GeneratedValue(generator = "uuid")
   @GenericGenerator(name = "uuid", strategy = "uuid2")
   private String uuid;
   
   
   @NotNull
   @Column(unique=true)   
   private String name;
   
   @NotNull
   private Trigger trigger;
   
   @ManyToOne(optional=false)
   private Endpoint source;
   
   @ManyToMany
   private List<Endpoint> targets;
   
   @ElementCollection(targetClass=TransferSendPolicies.class)
   @Enumerated(EnumType.STRING)
   private Set<TransferSendPolicies> transferSendPolicies = new HashSet<>();

   @ElementCollection(targetClass=TransferReceivePolicies.class)
   @Enumerated(EnumType.STRING)
   private Set<TransferReceivePolicies> transferReceivePolicies = new HashSet<>();

   
   @ManyToOne(optional=false)
   private AdministrativeApplication administrativeApplication;
   
   @ManyToOne(optional=false)
   private Tenant tenant;
   
   @OneToMany
   private List<Transformation> transformations = new ArrayList<>();
   
   @OneToOne
   private FileSelector fileSelector;
   
   
   public Transfer() {
      
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

   public Set<TransferSendPolicies> getTransferSendPolicies() {
      return transferSendPolicies;
   }

   public void setTransferSendPolicies(
         Set<TransferSendPolicies> transferSendPolicies) {
      this.transferSendPolicies = transferSendPolicies;
   }

   public Set<TransferReceivePolicies> getTransferReceivePolicies() {
      return transferReceivePolicies;
   }

   public void setTransferReceivePolicies(
         Set<TransferReceivePolicies> transferReceivePolicies) {
      this.transferReceivePolicies = transferReceivePolicies;
   }

   public FileSelector getFileSelector() {
      return fileSelector;
   }

   public void setFileSelector(FileSelector fileSelector) {
      this.fileSelector = fileSelector;
   }

   public String getUuid() {
      return uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Trigger getTrigger() {
      return trigger;
   }

   public void setTrigger(Trigger trigger) {
      this.trigger = trigger;
   }


}
