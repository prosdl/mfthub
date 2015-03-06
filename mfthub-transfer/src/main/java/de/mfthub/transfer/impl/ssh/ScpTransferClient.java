package de.mfthub.transfer.impl.ssh;

import java.util.Set;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.EndpointConfScp;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;
import de.mfthub.transfer.api.TransferClientSupport;
import de.mfthub.transfer.api.TransferReceiptInfo;
import de.mfthub.transfer.api.TransferSendInfo;
import de.mfthub.transfer.exception.TransmissionException;

public class ScpTransferClient extends TransferClientSupport<EndpointConfScp> {

   @Override
   public TransferReceiptInfo receive(Endpoint source, Delivery delivery,
         Set<TransferReceivePolicies> set) throws TransmissionException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public TransferSendInfo send(Endpoint target, Delivery delivery,
         Set<TransferSendPolicies> transferPolicies)
         throws TransmissionException {
      // TODO Auto-generated method stub
      return null;
   }

}
