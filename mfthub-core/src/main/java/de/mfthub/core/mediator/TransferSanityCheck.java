package de.mfthub.core.mediator;

import java.util.ArrayList;

import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.transfer.api.TransferClient;
import de.mfthub.transfer.exception.TransmissionMisconfigurationException;

public class TransferSanityCheck {

   private TransferClient<?> transferClient;
   private Transfer transfer;

   public TransferSanityCheck(Transfer transfer,
         TransferClient<?> transferClient) {
      this.transfer = transfer;
      this.transferClient = transferClient;
   }

   public void receiveSanityCheck()
         throws TransmissionMisconfigurationException {
      ArrayList<String> failureList = new ArrayList<>();

      for (TransferReceivePolicies p : transfer.getTransferReceivePolicies()) {
         if (!p.isImpliedBy(transferClient.getFeatures())) {
            failureList.add(String.format(
                  "This transfer client ('%s') does not support "
                        + "the policy: %s.", transferClient.getClass()
                        .getSimpleName(), p));
         }
      }
      if (!failureList.isEmpty()) {
         throw new TransmissionMisconfigurationException(failureList.toString());
      }
   }
}
