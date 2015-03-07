package de.mfthub.core.scheduler;

import org.quartz.SchedulerException;

import de.mfthub.model.entities.Transfer;

public interface MftScheduler {

   public abstract void scheduleTransfer(Transfer transfer)
         throws SchedulerException;

   public abstract void startScheduler() throws SchedulerException;

   public abstract void standbyScheduler() throws SchedulerException;

   public abstract void safeShutdownScheduler() throws SchedulerException;

   void scheduleRedeliveryJob() throws SchedulerException;

}