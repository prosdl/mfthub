package de.mfthub.core.api;

import de.mfthub.model.entities.Transfer;

public interface MftCoreAPI {

   public abstract void saveAndScheduleTransfer(Transfer transferIn)
         throws MftCoreAPIException;

   void bootstrapMft() throws MftCoreAPIException;

}