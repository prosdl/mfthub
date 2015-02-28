package de.mfthub.model.entities.enums;

import java.util.Set;

public interface HasClientFeatureRules {
   public boolean isImpliedBy(Set<TransferClientFeature> featureSet);
}
