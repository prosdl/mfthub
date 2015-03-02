package de.mfthub.model.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.mfthub.model.entities.enums.ProcessingType;

@Entity
public class Processing {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private Long id;
   
   @Enumerated(EnumType.STRING)
   private ProcessingType type;
   
   @OneToMany(cascade={CascadeType.ALL})
   private Set<ProcessingParamBinding> bindings = new HashSet<>();
   
   @JsonIgnore
   @ManyToOne
   private Transfer transfer;
   
   public Processing() {
      
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Set<ProcessingParamBinding> getBindings() {
      return bindings;
   }

   public void setBindings(Set<ProcessingParamBinding> bindings) {
      this.bindings = bindings;
   }

   public ProcessingType getType() {
      return type;
   }

   public void setType(ProcessingType type) {
      this.type = type;
   }

   public Transfer getTransfer() {
      return transfer;
   }

   public void setTransfer(Transfer transfer) {
      this.transfer = transfer;
   }
}
