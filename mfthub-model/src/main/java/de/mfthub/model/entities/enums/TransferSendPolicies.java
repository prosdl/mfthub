package de.mfthub.model.entities.enums;

import java.util.Set;

public enum TransferSendPolicies implements HasClientFeatureRules {
   LOCKSTRATEGY_PG_LEGACY {
      @Override
      public boolean isImpliedBy(Set<TransferClientFeature> set) {
         return set.contains(TransferClientFeature.TF_SUPPORTS_REMOVE_FILES);
      }
   },
   LOCKSTRATEGY_FILELOCK {
      @Override
      public boolean isImpliedBy(Set<TransferClientFeature> set) {
         return set.contains(TransferClientFeature.TF_SUPPORTS_NIO_FILE_LOCK);
      }
   },
   LOCKSTRATEGY_ATOMIC_MOVE {
      @Override
      public boolean isImpliedBy(Set<TransferClientFeature> set) {
         return set.contains(TransferClientFeature.TF_SUPPORTS_ATOMIC_MOVE);
      }
   },
   OVERWRITE_EXISTING {
      @Override
      public boolean isImpliedBy(Set<TransferClientFeature> set) {
         return true;
      }
   },
   NO_REDELIVERY {
      @Override
      public boolean isImpliedBy(Set<TransferClientFeature> set) {
         return true;
      }
   };

}
