package de.mfthub.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.Tenant;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, Long> {
   public Tenant findByName(String name);
}