package de.mfthub.model.entities.enums;

import java.util.Set;

public interface HasRecoverableErrorRule {
   public boolean isRecoveryAllowed(Set<TransferSendPolicies> sendPolicies,
         Set<TransferReceivePolicies> receivePolicies);
}
