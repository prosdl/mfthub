package de.mfthub.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TransferClientType {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private Long id;
   private String implementingClass;
}
