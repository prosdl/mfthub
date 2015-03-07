package de.mfthub.model.entities.enums;

import java.util.Set;

public enum TransferReceivePolicies implements HasTransferClientRule {
   ARCHIVE_FILES, LOCKSTRATEGY_PG_LEGACY {
      @Override
      public boolean isSupportedByClient(Set<TransferClientFeature> set) {
         return set.contains(TransferClientFeature.TF_SUPPORTS_REMOVE_FILES);
      }
   },
   LOCKSTRATEGY_FILELOCK {
      @Override
      public boolean isSupportedByClient(Set<TransferClientFeature> set) {
         return set.contains(TransferClientFeature.TF_SUPPORTS_NIO_FILE_LOCK);
      }
   },
   LOCKSTRATEGY_ATOMIC_MOVE {
      @Override
      public boolean isSupportedByClient(Set<TransferClientFeature> set) {
         return set.contains(TransferClientFeature.TF_SUPPORTS_ATOMIC_MOVE);
      }
   },
   RETRY_ALLOWED;

   // default
   @Override
   public boolean isSupportedByClient(Set<TransferClientFeature> set) {
      return true;
   }
}
