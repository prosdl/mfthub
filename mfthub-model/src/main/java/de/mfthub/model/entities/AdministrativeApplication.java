package de.mfthub.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class AdministrativeApplication {
   public static final String INTERNAL_ADMIN_APP_NAME = "internal";
   public static final AdministrativeApplication INTERNAL_ADMIN_APP = createInternal();
   
   private static AdministrativeApplication createInternal() {
      AdministrativeApplication a = new AdministrativeApplication();
      a.setName(INTERNAL_ADMIN_APP_NAME);
      a.setDescription("Interne Anwendung");
      return a;
   }

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long id;

   @NotNull
   @Column(unique = true)
   private String name;
   private String description;

   public AdministrativeApplication() {

   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      AdministrativeApplication other = (AdministrativeApplication) obj;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }

   
}
