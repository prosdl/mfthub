package de.mfthub.core.mediator;

import de.mfthub.core.mediator.exception.TransferExcecutionException;
import de.mfthub.model.entities.Delivery;

public interface TransferExecutor {

   public void receive(Delivery delivery) throws TransferExcecutionException;

   public void receive(String deliveryUuid) throws TransferExcecutionException;

   public void send(Delivery delivery) throws TransferExcecutionException;

   public void send(String deliveryUuid) throws TransferExcecutionException;

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