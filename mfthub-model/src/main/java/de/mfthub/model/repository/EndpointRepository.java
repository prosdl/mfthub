package de.mfthub.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.Endpoint;

@Repository
public interface EndpointRepository extends CrudRepository<Endpoint, Long> {
   public Endpoint findByEndpointKey(String endpointKey);
}