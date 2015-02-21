package de.mfthub.transfer.impl;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.LocalCpEndpointConfiguration;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;
import de.mfthub.transfer.api.MftPath;
import de.mfthub.transfer.api.MftPathException;
import de.mfthub.transfer.api.TransferClientSupport;
import de.mfthub.transfer.exception.TransmissionException;

public class LocalFilecopyTransferClient extends TransferClientSupport<LocalCpEndpointConfiguration> {

   private static Logger LOG = LoggerFactory.getLogger(LocalFilecopyTransferClient.class);
   
   public static class PrintFiles
      extends SimpleFileVisitor<Path> {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {
         // TODO
         System.out.println(file.toString());
         return FileVisitResult.CONTINUE;
      }
      
      @Override
      public FileVisitResult visitFileFailed(Path file, IOException exc)
            throws IOException {
         System.out.println("ERROR: " + exc.getMessage());
         return FileVisitResult.CONTINUE;
      }
   }

   public LocalFilecopyTransferClient() {
      initalizeFeatures(
            TransferClientFeature.TF_SUPPORTS_RECEIVE_FILES,
            TransferClientFeature.TF_SUPPORTS_SEND_FILES,
            TransferClientFeature.TF_SUPPORTS_REMOVE_FILES
            );
   }
   
   @Override
   public void send(Endpoint target, Delivery delivery, Set<TransferSendPolicies> policies)
         throws TransmissionException {
   }

   public Path mftPathToNIOPath(String basePath, MftPath mftPath) {
      return Paths.get(basePath, mftPath.toSegmentArray());
   }

   @Override
   public void receive(Endpoint source, Delivery delivery, Set<TransferReceivePolicies> policies)
         throws TransmissionException {
      LocalCpEndpointConfiguration conf =  (LocalCpEndpointConfiguration) source.getEndpointConfiguration();
      String from = conf.getDirectory();
      Path sourceDirectory = Paths.get(from);
      MftPath mftPath;
      try {
         mftPath = MftPath.outboundFrom(delivery);
      } catch (MftPathException e) {
         throw new TransmissionException("Problem building outbound path from delivery.", e);
      }
      Path outboundNIOPath = mftPathToNIOPath(configuration.getDirectory(), mftPath);
      
      try {
         Files.walkFileTree(sourceDirectory, new PrintFiles());
      } catch (IOException e) {
         throw new TransmissionException(
               String.format("Error while scanning directory %s", outboundNIOPath),e);
      }
   }

   
}
