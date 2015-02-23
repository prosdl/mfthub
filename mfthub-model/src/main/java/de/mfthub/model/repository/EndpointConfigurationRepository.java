package de.mfthub.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.mfthub.model.entities.EndpointConfiguration;

@Repository
public interface EndpointConfigurationRepository extends CrudRepository<EndpointConfiguration, Long> {
}