package de.mfthub.core.mediator;

import de.mfthub.core.mediator.exception.TransferExcecutionException;
import de.mfthub.core.mediator.exception.TransferMisconfigurationException;
import de.mfthub.model.entities.Delivery;
import de.mfthub.transfer.exception.TransmissionException;

public interface TransferExecutor {

   public void receive(Delivery delivery) throws TransmissionException,
         TransferMisconfigurationException;

   public void send(Delivery delivery) throws TransmissionException;

   public String createDeliveryForTransfer(String transferUUID) throws TransferExcecutionException;

}