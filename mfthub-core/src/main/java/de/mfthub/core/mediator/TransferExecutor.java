package de.mfthub.core.mediator;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Transfer;
import de.mfthub.transfer.exception.TransmissionException;
import de.mfthub.transfer.exception.TransmissionMisconfigurationException;

public interface TransferExecutor {

   public abstract void receive(Transfer transfer)
         throws TransmissionException, TransmissionMisconfigurationException;

   public abstract void send(Delivery delivery) throws TransmissionException;

}