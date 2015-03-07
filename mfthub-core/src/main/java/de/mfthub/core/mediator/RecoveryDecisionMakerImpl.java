package de.mfthub.core.mediator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.mfthub.core.mediator.exception.NonRecoverableErrorException;
import de.mfthub.core.mediator.exception.RecoverableErrorException;
import de.mfthub.core.mediator.exception.TransferExcecutionException;
import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.enums.DeliveryState;
import de.mfthub.model.repository.DeliveryRepository;
import de.mfthub.transfer.exception.TransmissionException;

@Component
public class RecoveryDecisionMakerImpl implements RecoveryDecisionMaker {
   
   @Autowired
   private DeliveryRepository deliveryRepository;

   @Override
   public void throwTEE(TransmissionException transmissionException,
         Delivery delivery) throws TransferExcecutionException {
      if (transmissionException.getErrorCode().isRecoveryAllowed(
            delivery.getTransfer().getTransferSendPolicies(),
            delivery.getTransfer().getTransferReceivePolicies())) {
         
         // TODO no endless loop: check delivery.stateChanges how often we retried ...
         
         deliveryRepository.updateDeliveryState(delivery, DeliveryState.AUTO_RECOVERABLE_ERROR,
               transmissionException.getMessage(), null);         
         throw new RecoverableErrorException(transmissionException);
      } else {
         deliveryRepository.updateDeliveryState(delivery, DeliveryState.NON_AUTO_RECOVERABLE_ERROR,
               transmissionException.getMessage(), null);         
         throw new NonRecoverableErrorException(transmissionException);
      }
   }

}
