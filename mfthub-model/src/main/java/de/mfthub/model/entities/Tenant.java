package de.mfthub.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Tenant {
   
   public static final String INTERNAL_TENANT_NAME = "internal";
   public static final String INTERNAL_TENANT_EXTID = "000";
   public static final Tenant INTERNAL_TENANT = createInternal();
   
   private static Tenant createInternal() {
      Tenant t = new Tenant();
      t.setName(INTERNAL_TENANT_NAME);
      t.setExternalId(INTERNAL_TENANT_EXTID);
      t.setDescription("Interner Transfer");
      return t;
   }
   
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)   
   private Long id;
   @NotNull
   @Column(unique = true)
   private String name;
   private String description;
   
   @Column(unique = true)
   private String externalId;
   
   public Tenant() {
      
   }
   
   @Override
   public String toString() {
      return name;
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

   public String getExternalId() {
      return externalId;
   }

   public void setExternalId(String externalId) {
      this.externalId = externalId;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
            + ((externalId == null) ? 0 : externalId.hashCode());
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
      Tenant other = (Tenant) obj;
      if (externalId == null) {
         if (other.externalId != null)
            return false;
      } else if (!externalId.equals(other.externalId))
         return false;
      if (name == null) {
         if (other.name != null)
            return false;
      } else if (!name.equals(other.name))
         return false;
      return true;
   }
   
   
}
