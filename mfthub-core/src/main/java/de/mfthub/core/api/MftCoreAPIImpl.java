package de.mfthub.core.api;

import javax.transaction.Transactional;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.mfthub.core.scheduler.MftScheduler;
import de.mfthub.model.entities.Transfer;
import de.mfthub.model.repository.TransferRepository;
import de.mfthub.storage.conf.StorageConfiguration;

@Service
@Transactional
public class MftCoreAPIImpl implements MftCoreAPI {
   
   @Autowired
   private MftScheduler mftScheduler;
   
   @Autowired
   private TransferRepository transferRepository;

   @Value("${mft.storage.root}")
   private String mftStorageRoot;

   
   /* (non-Javadoc)
    * @see de.mfthub.core.api.MftCoreAPI#saveAndScheduleTransfer(de.mfthub.model.entities.Transfer)
    */
   @Override
   public void saveAndScheduleTransfer(Transfer transferIn) throws MftCoreAPIException {
      Transfer transfer;
      try {
         transfer = transferRepository.saveOrUpdate(transferIn);
      } catch (Exception e) {
         throw new MftCoreAPIException("Problem while trying to persist transfer.", e);
      }
      try {
         mftScheduler.scheduleTransfer(transfer);
      } catch (SchedulerException e) {
         throw new MftCoreAPIException("Problem while trying to schedule transfer.", e);
      }
   }
   
   
   @Override
   public void bootstrapMft() throws MftCoreAPIException {
      StorageConfiguration.INSTANCE.initialize(mftStorageRoot);
      try {
         mftScheduler.scheduleRedeliveryJob();
      } catch (SchedulerException e) {
         throw new MftCoreAPIException("Couldn't schedule redelivery job", e);
      }
   }
}
