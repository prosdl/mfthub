package de.mfthub.model.repository;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import de.mfthub.model.entities.AdministrativeApplication;
import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.Tenant;
import de.mfthub.model.entities.Transfer;

@Repository
public class TransferRepositoryImpl implements TransferRepositoryCustom {

   @Autowired
   private EntityManager em;

   @Autowired
   private TenantRepository tenantRepository;

   @Autowired
   private AdministrativeApplicationRepository administrativeApplicationRepository;
   
   public <T> void addPKIfUniqeFieldExists(T obj, String uniqueField) {
      Field idFieldT = ReflectionUtils.findField(obj.getClass(), "id");
      idFieldT.setAccessible(true);   
      Long id = (Long) ReflectionUtils.getField(idFieldT, obj);
      if (id == null) {
         Field uniqueFieldT = ReflectionUtils.findField(obj.getClass(), uniqueField);
         uniqueFieldT.setAccessible(true);
         Object objValOfUniqeField = ReflectionUtils.getField(uniqueFieldT, obj);
         Query query = em.createQuery("select x from " + obj.getClass().getSimpleName()
               + " x where " + uniqueField  + " = :val");
         query.setParameter("val", objValOfUniqeField);
         @SuppressWarnings("unchecked")
         List<T> existing = query.getResultList();
         
         if (existing.size() == 1) {
            T existingEntity = existing.get(0);
            Object exIdValue = ReflectionUtils.getField(idFieldT, existingEntity);
            ReflectionUtils.setField(idFieldT, obj, exIdValue);
         }
      }
   }

   @Override
   public Transfer saveOrUpdate(Transfer transfer) {
      if (transfer.getAdministrativeApplication() == null) {
         transfer
               .setAdministrativeApplication(AdministrativeApplication.INTERNAL_ADMIN_APP);
      }
      if (transfer.getTenant() == null) {
         transfer.setTenant(Tenant.INTERNAL_TENANT);
      }
      
      addPKIfUniqeFieldExists(transfer.getTenant(), "name");
      addPKIfUniqeFieldExists(transfer.getAdministrativeApplication(), "name");
      addPKIfUniqeFieldExists(transfer.getSource(), "endpointKey");
      for (Endpoint target: transfer.getTargets()) {
         addPKIfUniqeFieldExists(target, "endpointKey");
      }

      return em.merge(transfer);
   }

}
