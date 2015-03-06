package de.mfthub.transfer.api;

import java.io.IOException;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.EndpointConfiguration;
import de.mfthub.model.entities.enums.TransferClientFeature;
import de.mfthub.storage.folder.MftFolder;
import de.mfthub.storage.folder.MftPathException;
import de.mfthub.transfer.exception.TransmissionException;

public abstract class TransferClientSupport<E extends EndpointConfiguration>
      implements TransferClient<E> {

   protected E configuration;
   protected Set<TransferClientFeature> features;
   
   protected void initalizeFeatures(TransferClientFeature... feature) {
       features = ImmutableSet.<TransferClientFeature>builder().add(feature).build();
   }
   
   @Override
   public Set<TransferClientFeature> getFeatures() {
      return features;
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
   
   public MftFolder getOutbound(Delivery delivery) throws TransmissionException {
      try {
         return MftFolder.createOutboundFromDelivery(delivery);
      } catch (IOException e) {
         throw new TransmissionException(
               String.format("Error while creating inbound box for delivery %s", delivery),e);
      } catch (MftPathException e) {
         throw new TransmissionException(
               String.format("Error while constructing mft path for delivery %s", delivery),e);
      }      
   }

   public MftFolder getInbound(Delivery delivery) throws TransmissionException {
      try {
         return MftFolder.createInboundFromDelivery(delivery);
      } catch (IOException e) {
         throw new TransmissionException(
               String.format("Error while creating inbound box for delivery %s", delivery),e);
      } catch (MftPathException e) {
         throw new TransmissionException(
               String.format("Error while constructing mft path for delivery %s", delivery),e);
      }      
   }
   
}
