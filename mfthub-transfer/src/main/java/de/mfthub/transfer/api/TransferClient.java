package de.mfthub.transfer.api;

import java.util.Set;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.EndpointConfiguration;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;
import de.mfthub.transfer.exception.TransmissionException;

public interface TransferClient<E extends EndpointConfiguration> {

   // @formatter:off
   public enum TransferClientFeature {
      TF_SUPPORTS_SEND_FILES, 
      TF_SUPPORTS_RECEIVE_FILES, 
      TF_SUPPORTS_REMOVE_FILES
   }
   // @formatter:on

   public boolean supportsFeature(TransferClientFeature transferClientFeature);

   public void setConfiguration(EndpointConfiguration configuration);

   public E getConfiguration();

   public void receive(Endpoint source, Delivery delivery, Set<TransferReceivePolicies> set)
         throws TransmissionException;

   public void send(Endpoint target, Delivery delivery, Set<TransferSendPolicies> transferPolicies)
         throws TransmissionException;
}
