package de.mfthub.core.mediator;

import de.mfthub.core.mediator.exception.TransferExcecutionException;
import de.mfthub.core.mediator.exception.TransferMisconfigurationException;
import de.mfthub.model.entities.Delivery;
import de.mfthub.transfer.exception.TransmissionException;

public interface TransferExecutor {

   public void receive(Delivery delivery) throws TransmissionException,
         TransferMisconfigurationException;

   public void receive(String deliveryUuid) throws TransmissionException,
         TransferExcecutionException;

   public void send(Delivery delivery) throws TransmissionException;

   public void send(String deliveryUuid) throws TransmissionException,
         TransferExcecutionException;

   public String createDeliveryForTransfer(String transferUUID)
         throws TransferExcecutionException;

   void prepareProcessing(String deliveryUuid)
         throws TransferExcecutionException;

   void prepareProcessing(Delivery delivery)
         throws TransferExcecutionException;

   void process(String deliveryUuid) throws TransferExcecutionException;

   void process(Delivery delivery) throws TransferExcecutionException;

   void prepareSend(String deliveryUuid) throws TransferExcecutionException;

   void prepareSend(Delivery delivery) throws TransferExcecutionException;

}