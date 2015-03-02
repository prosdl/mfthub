package de.mfthub.storage.folder;

import java.io.IOException;
import java.nio.file.Path;

import de.mfthub.model.entities.Delivery;
import de.mfthub.storage.nio.NioFileTools;

public class MftFolder {

   private String basePath = "/tmp"; // FIXME from application.properties + @value
   private MftFolderPath mftPath;
   private Path path;

   private MftFolder(MftFolderPath mftPath) throws IOException {
      this.mftPath = mftPath;
      path = NioFileTools.mftPathToNIOPath(basePath, mftPath);
      NioFileTools.createDirectoryIfNotExists(path);
   }

   public static MftFolder createOutboundFromDelivery(Delivery delivery)
         throws IOException, MftPathException {
      return new MftFolder(MftFolderPath.outboundFrom(delivery));
   }

   public static MftFolder createInboundFromDelivery(Delivery delivery)
         throws IOException, MftPathException {
      return new MftFolder(MftFolderPath.inboundFrom(delivery));
   }
   
   public static MftFolder createProcessingOutFromDelivery(Delivery delivery)
         throws IOException, MftPathException {
      return new MftFolder(MftFolderPath.processingOutFrom(delivery));
   }
   
   public static MftFolder createProcessingInFromDelivery(Delivery delivery)
         throws IOException, MftPathException {
      return new MftFolder(MftFolderPath.processingInFrom(delivery));
   }

   public String getBasePath() {
      return basePath;
   }

   public MftFolderPath getMftPath() {
      return mftPath;
   }

   public Path getPath() {
      return path;
   }

}
