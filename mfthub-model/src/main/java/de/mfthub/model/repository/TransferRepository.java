package de.mfthub.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.Tenant;
import de.mfthub.model.entities.Transfer;

@Repository
public interface TransferRepository extends CrudRepository<Transfer, String>, TransferRepositoryCustom {
   public Tenant findByName(String name);
}