package de.mfthub.model.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Delivery {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private Long id;
   private Transfer transfer;
   private Date initiated;
   private Date finished;
   private String errorDetails;
}
