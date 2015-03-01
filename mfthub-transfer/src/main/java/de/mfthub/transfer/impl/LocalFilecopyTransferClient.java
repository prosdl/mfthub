package de.mfthub.transfer.impl;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import de.mfthub.model.entities.Delivery;
import de.mfthub.model.entities.Endpoint;
import de.mfthub.model.entities.EndpointConfLocalCp;
import de.mfthub.model.entities.enums.TransferClientFeature;
import de.mfthub.model.entities.enums.TransferReceivePolicies;
import de.mfthub.model.entities.enums.TransferSendPolicies;
import de.mfthub.transfer.api.TransferClientSupport;
import de.mfthub.transfer.api.TransferReceiptInfo;
import de.mfthub.transfer.exception.TransmissionException;
import de.mfthub.transfer.storage.MftFolder;
import de.mfthub.transfer.storage.MftPathException;

public class LocalFilecopyTransferClient extends TransferClientSupport<EndpointConfLocalCp> {

   private static Logger LOG = LoggerFactory.getLogger(LocalFilecopyTransferClient.class);
   
   public static class MoveFilesVisitor
      extends SimpleFileVisitor<Path> {
      
      private AntPathMatcher antPathMatcher;
      private String pattern;
      private Path sourcePath;
      private Path targetPath;
      
      private long fileCount = 0;
      private long byteCount = 0;
      
      public MoveFilesVisitor(String pattern, Path sourcePath, Path targetPath) {
         antPathMatcher = new AntPathMatcher();
         antPathMatcher.setCachePatterns(true);
         this.pattern = pattern;
         this.sourcePath = sourcePath;
         this.targetPath = targetPath;
      }
      
      public long getFileCount() {
         return fileCount;
      }

      public long getByteCount() {
         return byteCount;
      }

      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {
         Path fileRelToSourcePath = sourcePath.relativize(file);
         LOG.info("Found: '{}'.", fileRelToSourcePath.toString());
         if (antPathMatcher.match(pattern, fileRelToSourcePath.toString())) {
            Path targetFile = targetPath.resolve(fileRelToSourcePath);
            LOG.info("Moving '{}' to '{}'.", file.toString(), targetFile.toString());
            Files.createDirectories(targetFile.getParent());
            Files.copy(file, targetFile, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
            fileCount++;
            byteCount += Files.size(targetFile);
         }
         return FileVisitResult.CONTINUE;
      }
      
      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            throws IOException {
         Path fileRelToSource = sourcePath.relativize(dir);
         LOG.info("Found: '{}'.", fileRelToSource.toString());
         if (antPathMatcher.match(pattern, fileRelToSource.toString())) {
            Path targetFile = targetPath.resolve(fileRelToSource);
            LOG.info("Moving '{}' to '{}'.", dir.toString(), targetFile.toString());
         }
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

   @Override
   public TransferReceiptInfo receive(Endpoint source, Delivery delivery, Set<TransferReceivePolicies> policies)
         throws TransmissionException {
      EndpointConfLocalCp conf =  (EndpointConfLocalCp) source.getEndpointConfiguration();
      String from = conf.getDirectory();
      Path sourceDirectory = Paths.get(from);
      
      MftFolder inbound;
      try {
         inbound = MftFolder.createInboundFromDelivery(delivery);
      } catch (IOException e) {
         throw new TransmissionException(
               String.format("Error while creating outbound box for delivery %s", delivery),e);
      } catch (MftPathException e) {
         throw new TransmissionException(
               String.format("Error while constructing mft path for delivery %s", delivery),e);
      }

      MoveFilesVisitor visitor = new MoveFilesVisitor(delivery.getTransfer().getFileSelector().getFilenameExpression(),sourceDirectory, inbound.getPath());
      try {
         Files.walkFileTree(sourceDirectory, visitor);
         LOG.info("Moved files: {}, total size in bytes: {}.", visitor.getFileCount(), visitor.getByteCount());
      } catch (IOException e) {
         throw new TransmissionException(
               String.format("Error while scanning directory %s", sourceDirectory),e);
      }
      
      TransferReceiptInfo info = new TransferReceiptInfo();
      info.setNumberOfFilesReceived(visitor.getFileCount());
      info.setTotalBytesReceived(visitor.getByteCount());
      info.setInboundFolder(inbound.getPath().toString());
      return info;
   }

   
}
