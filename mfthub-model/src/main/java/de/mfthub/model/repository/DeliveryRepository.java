package de.mfthub.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.Delivery;

@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
}