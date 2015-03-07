package de.mfthub.core.mediator;

import de.mfthub.core.mediator.exception.TransferExcecutionException;
import de.mfthub.model.entities.Delivery;
import de.mfthub.transfer.exception.TransmissionException;

public interface RecoveryDecisionMaker {
   void throwTEE(TransmissionException transmissionException, Delivery delivery)
         throws TransferExcecutionException;
}
