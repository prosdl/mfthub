package de.mfthub.model.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.Tenant;

@Repository
public interface TenantRepository extends CrudRepository<Tenant, Long>, TenantRepositoryCustom {
   public Tenant findByName(String name);
   public Page<Tenant> findByName(String name, Pageable pageable);
   
   public Page<Tenant> findByNameLike(String name, Pageable pageable);
}