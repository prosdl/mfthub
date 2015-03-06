package de.mfthub.transfer.impl.local;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.EndpointConfLocalCp;
import de.mfthub.model.entities.enums.TransferClientFeature;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;
import de.mfthub.storage.folder.MftFolder;
import de.mfthub.storage.nio.MoveOrCopyFilesVisitor;
import de.mfthub.transfer.api.TransferClientSupport;
import de.mfthub.transfer.api.TransferReceiptInfo;
import de.mfthub.transfer.api.TransferSendInfo;
import de.mfthub.transfer.exception.TransmissionException;

public class LocalFilecopyTransferClient extends TransferClientSupport<EndpointConfLocalCp> {

   private static Logger LOG = LoggerFactory.getLogger(LocalFilecopyTransferClient.class);
   
   public LocalFilecopyTransferClient(EndpointConfLocalCp conf) {
      super(conf);
      initalizeFeatures(
            TransferClientFeature.TF_SUPPORTS_RECEIVE_FILES,
            TransferClientFeature.TF_SUPPORTS_SEND_FILES,
            TransferClientFeature.TF_SUPPORTS_REMOVE_FILES
            );
   }
   
   @Override
   public TransferSendInfo send(Endpoint target, Delivery delivery, Set<TransferSendPolicies> policies)
         throws TransmissionException {
      String to = getConfiguration().getDirectory();
      Path targetDirectory = Paths.get(to);  
      
      MftFolder outbound = getOutbound(delivery);

      MoveOrCopyFilesVisitor visitor = new MoveOrCopyFilesVisitor("**/*.*",outbound.getPath(),targetDirectory);
      try {
         Files.walkFileTree(outbound.getPath(), visitor);
         LOG.info("Moved files: {}, total size in bytes: {}.", visitor.getFileCount(), visitor.getByteCount());
      } catch (IOException e) {
         throw new TransmissionException(
               String.format("Error while copying to directory %s", targetDirectory.toString()),e);
      }

      TransferSendInfo info = new TransferSendInfo();
      info.setNumberOfFilesSend(visitor.getFileCount());
      info.setTotalBytesSend(visitor.getByteCount());
      info.setOutboundFolder(outbound.getPath().toString());
      return info;
   }

   @Override
   public TransferReceiptInfo receive(Endpoint source, Delivery delivery, Set<TransferReceivePolicies> policies)
         throws TransmissionException {
      String from = getConfiguration().getDirectory();
      Path sourceDirectory = Paths.get(from);
      
      MftFolder inbound = getInbound(delivery);

      MoveOrCopyFilesVisitor visitor = new MoveOrCopyFilesVisitor(delivery.getTransfer().getFileSelector().getFilenameExpression(),sourceDirectory, inbound.getPath());
      try {
         Files.walkFileTree(sourceDirectory, visitor);
         LOG.info("Moved files: {}, total size in bytes: {}.", visitor.getFileCount(), visitor.getByteCount());
      } catch (IOException e) {
         throw new TransmissionException(
               String.format("Error while copying from directory %s", sourceDirectory),e);
      }
      
      TransferReceiptInfo info = new TransferReceiptInfo();
      info.setNumberOfFilesReceived(visitor.getFileCount());
      info.setTotalBytesReceived(visitor.getByteCount());
      info.setInboundFolder(inbound.getPath().toString());
      return info;
   }

   
}
