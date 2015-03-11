package de.mfthub.model.repository;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.Tenant;

@Repository
public class TenantRepositoryImpl implements TenantRepositoryCustom {

   @Override
   public Page<Tenant> findFilteredByField(String field, String name) {
      // TODO Auto-generated method stub
      return null;
   }
}