package de.mfthub.model.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.mfthub.model.entities.enums.FileSelectorStrategy;

@Entity
public class FileSelector {
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private Long id;
   
   @Enumerated(EnumType.STRING)
   private FileSelectorStrategy fileSelectorStrategy;
   private String filenameExpression;
   private boolean onlyNewest;
   
   public FileSelector() {
      
   }
   
   public Long getId() {
      return id;
   }
   public void setId(Long id) {
      this.id = id;
   }
   public FileSelectorStrategy getFileSelectorStrategy() {
      return fileSelectorStrategy;
   }
   public void setFileSelectorStrategy(FileSelectorStrategy fileSelectorStrategy) {
      this.fileSelectorStrategy = fileSelectorStrategy;
   }
   public String getFilenameExpression() {
      return filenameExpression;
   }
   public void setFilenameExpression(String filenameExpression) {
      this.filenameExpression = filenameExpression;
   }
   public boolean isOnlyNewest() {
      return onlyNewest;
   }
   public void setOnlyNewest(boolean onlyNewest) {
      this.onlyNewest = onlyNewest;
   }
   
   
}
