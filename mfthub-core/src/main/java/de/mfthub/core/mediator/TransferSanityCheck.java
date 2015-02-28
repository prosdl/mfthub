package de.mfthub.core.mediator;

import java.util.ArrayList;

import de.mfthub.model.entities.Transfer;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.transfer.api.TransferClient;
import de.mfthub.transfer.api.TransferClient.TransferClientFeature;
import de.mfthub.transfer.exception.TransmissionMisconfigurationException;

/**
 * Simple "fluent" API for an even simpler "rules engine".
 * 
 * @author prosdl
 *
 */
public class TransferSanityCheck {
   
   private TransferClient<?> transferClient;
   private Transfer transfer;
   private ArrayList<String> failureList = new ArrayList<>();
   
   
   public TransferSanityCheck(Transfer transfer, TransferClient<?> transferClient) {
      this.transfer = transfer;
      this.transferClient = transferClient;
   }
   
   private class Endpoint {
      public Result needs(TransferClientFeature feature) {
         if (transferClient.supportsFeature(feature)) {
            return new SuccessResult();
         } else {
            return new FailResult();
         }
      }
   }

   private interface Result {
      public void otherwiseFailWith(String msgFormat);
   }

   private class SuccessResult implements Result {
      public void otherwiseFailWith(String msgFormat) {
      }
   }
   
   private class FailResult implements Result {
      public void otherwiseFailWith(String msgFormat) {
         failureList.add(String.format(msgFormat, transferClient.toString()));
      }
   }
   
   private class TransferReception {
      public boolean requires(TransferReceivePolicies policy) {
         return transfer.getTransferReceivePolicies().contains(policy);
      }
   }
   
   private Endpoint thisEndpoint = new Endpoint();
   private TransferReception thisFileReception = new TransferReception();
   
   private void receiveRules() {
      // @formatter:off

      thisEndpoint.
         needs(TransferClientFeature.TF_SUPPORTS_RECEIVE_FILES).
            otherwiseFailWith(
               "This transfer client ('%s') does not support receiving files from a source endpoint."
                  );
   
      if (thisFileReception.requires(TransferReceivePolicies.LOCKSTRATEGY_PG_LEGACY)) {
         thisEndpoint.
            needs(TransferClientFeature.TF_SUPPORTS_REMOVE_FILES).
               otherwiseFailWith(
                     "This transfer client ('%s') does not support PG locking.");   
      }
      
      if (thisFileReception.requires(TransferReceivePolicies.LOCKSTRATEGY_ATOMIC_MOVE)) {
         thisEndpoint.
            needs(TransferClientFeature.TF_SUPPORTS_ATOMIC_MOVE).
               otherwiseFailWith(
                     "This transfer client ('%s') does not support atomic move/rename.");
      }
      // @formatter:on
   }
   
   public void receiveSanityCheck() throws TransmissionMisconfigurationException {
      failureList.clear();
      receiveRules();
      if (!failureList.isEmpty()) {
         throw new TransmissionMisconfigurationException(failureList.toString());
      }
   }
}
