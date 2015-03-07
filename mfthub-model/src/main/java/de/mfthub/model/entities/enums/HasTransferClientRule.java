package de.mfthub.model.entities.enums;

import java.util.Set;

public interface HasTransferClientRule {
   public boolean isSupportedByClient(Set<TransferClientFeature> featureSet);
}
