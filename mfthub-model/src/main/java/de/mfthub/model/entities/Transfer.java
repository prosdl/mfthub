package de.mfthub.model.entities;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.mfthub.model.entities.enums.TransferPolicies;

@Entity
public class Transfer {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private Long id;
   private Endpoint source;
   private List<Endpoint> targets;
   private Set<TransferPolicies> transferPolicies;
   private AdministrativeApplication administrativeApplication;
   private Tenant tenant;
   private List<Transformation> transformations;
   
}
