package de.mfthub.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.AdministrativeApplication;

@Repository
public interface AdministrativeApplicationRepository extends CrudRepository<AdministrativeApplication, Long> {
   public AdministrativeApplication findByName(String name);
}