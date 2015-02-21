package de.mfthub.transfer.api;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import de.mfthub.model.entities.EndpointConfiguration;

public abstract class TransferClientSupport<E extends EndpointConfiguration>
      implements TransferClient<E> {

   protected E configuration;
   protected Set<TransferClientFeature> features;
   
   protected void initalizeFeatures(TransferClientFeature... feature) {
       features = ImmutableSet.<TransferClientFeature>builder().add(feature).build();
   }
   
   public boolean supportsFeature(TransferClientFeature transferClientFeature) {
      return features.contains(transferClientFeature);
   }

   @SuppressWarnings("unchecked")
   @Override
   public void setConfiguration(EndpointConfiguration configuration) {
      this.configuration = (E) configuration;
   }

   @Override
   public E getConfiguration() {
      return configuration;
   }

}
