package de.mfthub.storage.folder;

import java.io.IOException;
import java.nio.file.Path;

import de.mfthub.model.entities.Delivery;
import de.mfthub.storage.conf.StorageConfiguration;
import de.mfthub.storage.nio.NioFileTools;

public class MftFolder {

   private String basePath = StorageConfiguration.INSTANCE.getStorageRootDirectory();
   private MftPathBuilder mftPath;
   private Path path;

   private MftFolder(MftPathBuilder mftPath) throws IOException {
      this.mftPath = mftPath;
      path = NioFileTools.mftPathToNIOPath(basePath, mftPath);
      NioFileTools.createDirectoryIfNotExists(path);
   }

   public static MftFolder createOutboundFromDelivery(Delivery delivery)
         throws IOException, MftPathException {
      return new MftFolder(MftPathBuilder.outboundFrom(delivery));
   }

   public static MftFolder createInboundFromDelivery(Delivery delivery)
         throws IOException, MftPathException {
      return new MftFolder(MftPathBuilder.inboundFrom(delivery));
   }
   
   public static MftFolder createProcessingOutFromDelivery(Delivery delivery)
         throws IOException, MftPathException {
      return new MftFolder(MftPathBuilder.processingOutFrom(delivery));
   }
   
   public static MftFolder createProcessingInFromDelivery(Delivery delivery)
         throws IOException, MftPathException {
      return new MftFolder(MftPathBuilder.processingInFrom(delivery));
   }

   public String getBasePath() {
      return basePath;
   }

   public MftPathBuilder getMftPath() {
      return mftPath;
   }

   public Path getPath() {
      return path;
   }

}
