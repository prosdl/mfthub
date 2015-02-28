package de.mfthub.model.repository;

import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.Transfer;

@Repository
public interface TransferRepositoryCustom {
   public Transfer saveOrUpdate(Transfer transfer);
}