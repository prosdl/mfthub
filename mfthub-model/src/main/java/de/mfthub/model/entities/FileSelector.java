package de.mfthub.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.mfthub.model.entities.enums.FileSelectorStrategy;

@Entity
public class FileSelector {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private Long id;
   
   private FileSelectorStrategy fileSelectorStrategy;
   private String filenameExpression;
   private boolean onlyNewest;
   
}
