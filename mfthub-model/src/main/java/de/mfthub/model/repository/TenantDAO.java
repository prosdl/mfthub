package de.mfthub.model.repository;

import org.springframework.stereotype.Component;

import de.mfthub.model.entities.Tenant;

@Component
public class TenantDAO extends GenericDAO<Tenant, Long> implements ITenantDAO {

}
