package de.mfthub.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class EndpointConfiguration {
   @Id
   @GeneratedValue(strategy = GenerationType.TABLE)
   protected Long id;
   protected String directory;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getDirectory() {
      return directory;
   }

   public void setDirectory(String directory) {
      this.directory = directory;
   }

}
