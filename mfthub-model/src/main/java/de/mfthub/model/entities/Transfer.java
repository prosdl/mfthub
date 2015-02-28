package de.mfthub.model.entities;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
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

import com.google.common.collect.Sets;

import de.mfthub.model.entities.enums.FileSelectorStrategy;
import de.mfthub.model.entities.enums.Protocol;
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
   
   @ManyToOne(optional=false, cascade={CascadeType.PERSIST, CascadeType.MERGE})
   private Endpoint source;
   
   @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
   private List<Endpoint> targets;
   
   @ElementCollection(targetClass=TransferSendPolicies.class)
   @Enumerated(EnumType.STRING)
   private Set<TransferSendPolicies> transferSendPolicies = new HashSet<>();

   @ElementCollection(targetClass=TransferReceivePolicies.class)
   @Enumerated(EnumType.STRING)
   private Set<TransferReceivePolicies> transferReceivePolicies = new HashSet<>();

   
   @ManyToOne(optional=false,  cascade={CascadeType.PERSIST, CascadeType.MERGE})
   private AdministrativeApplication administrativeApplication;
   
   @ManyToOne(optional=false, cascade={CascadeType.PERSIST, CascadeType.MERGE})
   private Tenant tenant;
   
   @OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
   private List<Transformation> transformations = new ArrayList<>();
   
   @OneToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE})
   private FileSelector fileSelector;
   
   
   public static class Builder {
      private String uuid;
      private String name;
      private Trigger trigger;
      private Endpoint source;
      private List<Endpoint> targets;
      private Set<TransferSendPolicies> transferSendPolicies = new HashSet<>();
      private Set<TransferReceivePolicies> transferReceivePolicies = new HashSet<>();
      private AdministrativeApplication administrativeApplication;
      private Tenant tenant;
      private List<Transformation> transformations = new ArrayList<>();
      private FileSelector fileSelector;

      public Builder(String name) {
         this.name = name;
         this.tenant = Tenant.INTERNAL_TENANT;
         this.administrativeApplication = AdministrativeApplication.INTERNAL_ADMIN_APP;
      }
      
      public Builder forTenant(Tenant tenant) {
         this.tenant = tenant;
         return this;
      }
      
      public Builder forApplication(AdministrativeApplication app) {
         this.administrativeApplication = app;
         return this;
      }
      
      public Builder withRandamUUID() {
         this.uuid = UUID.randomUUID().toString();
         return this;
      }
      
      public Builder fromSource(Endpoint endpoint) {
         this.source = endpoint;
         return this;
      }
      
      public Builder fromSource(String uri) throws URISyntaxException {
         this.source = Protocol.buildEndpointFromURI(uri);
         return this;
      }
      public Builder fromNamedSource(String name, String uri) throws URISyntaxException {
         this.source = Protocol.buildEndpointFromURI(name,uri);
         return this;
      }
      
      public Builder toTargets(Endpoint... endpoint) {
         this.targets = Arrays.asList(endpoint);
         return this;
      }
      
      public Builder toTargets(String... uris) throws URISyntaxException {
         targets = new ArrayList<>();
         for (String uri: uris) {
            targets.add(Protocol.buildEndpointFromURI(uri));
         }
         return this;
      }
      
      public Builder withCronSchedule(String cron) {
         this.trigger = new Trigger();
         this.trigger.setCronExpresion(cron);
         return this;
      }
      
      public Builder files(String filename) {
         this.fileSelector = new FileSelector();
         this.fileSelector.setFilenameExpression(filename);
         this.fileSelector.setFileSelectorStrategy(FileSelectorStrategy.ANT_STYLE);
         return this;
      }
      
      public Builder usingSendPolicies(TransferSendPolicies... transferSendPolicies) {
         this.transferSendPolicies = Sets.newHashSet(transferSendPolicies);
         return this;
      }
      
      public Builder usingReceivePolicies(TransferReceivePolicies... transferReceivePolicies) {
         this.transferReceivePolicies = Sets.newHashSet(transferReceivePolicies);
         return this;
      }
      
      public Transfer build() {
         return new Transfer(this);
      }
   }
   public Transfer() {
   }
   public Transfer(Builder builder) {
      this.administrativeApplication = builder.administrativeApplication;
      this.fileSelector = builder.fileSelector;
      this.name = builder.name;
      this.source = builder.source;
      this.targets = builder.targets;
      this.tenant = builder.tenant;
      this.transferReceivePolicies = builder.transferReceivePolicies;
      this.transferSendPolicies = builder.transferSendPolicies;
      this.transformations = builder.transformations;
      this.trigger = builder.trigger;
      this.uuid = builder.uuid;
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
