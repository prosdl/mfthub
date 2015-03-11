package de.mfthub.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.Tenant;

@Repository
public interface TenantRepositoryCustom  {
   public Page<Tenant> findFilteredByField(String field, String name);

}