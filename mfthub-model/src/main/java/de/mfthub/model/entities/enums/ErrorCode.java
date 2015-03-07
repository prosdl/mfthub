package de.mfthub.model.entities.enums;

import java.util.Set;

public enum ErrorCode implements HasRecoverableErrorRule {
   TRANSMISSION_COULDNT_RECEIVE("General error while trying to receive files.") {

      @Override
      public boolean isRecoveryAllowed(Set<TransferSendPolicies> sendPolicies,
            Set<TransferReceivePolicies> receivePolicies) {
         return receivePolicies.contains(TransferReceivePolicies.RETRY_ALLOWED);
      }
   },
   TRANSMISSION_COULDNT_SEND("General error while trying to send files.") {
      @Override
      public boolean isRecoveryAllowed(Set<TransferSendPolicies> sendPolicies,
            Set<TransferReceivePolicies> receivePolicies) {
         return sendPolicies.contains(TransferSendPolicies.RETRY_ALLOWED);
      }
   },
   INTERNAL_STORAGE("General error with internal file storage.");

   // default
   @Override
   public boolean isRecoveryAllowed(Set<TransferSendPolicies> sendPolicies,
         Set<TransferReceivePolicies> receivePolicies) {
      return false;
   }

   private String description;

   private ErrorCode(String description) {
      this.description = description;
   }

   // private abstract boolean

   public String getDescription() {
      return this.description;
   }

}
