package de.mfthub.processing.impl;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.tar.TarArchiver;
import org.codehaus.plexus.archiver.tar.TarArchiver.TarCompressionMethod;
import org.codehaus.plexus.archiver.util.DefaultFileSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.mfthub.model.entities.Delivery;
import de.mfthub.processing.api.Processor;
import de.mfthub.processing.api.exception.ProcessorException;
import de.mfthub.storage.folder.MftFolder;
import de.mfthub.storage.folder.MftPathException;

public class CompressProcessor implements Processor {
   private static Logger LOG = LoggerFactory.getLogger(CompressProcessor.class);
   private static final String FILE_PATTERN_DEFAULT = "**/*.*";
   
//   @ProcessorParam(optional=false, description="Which files to compress.")
   private String filePattern;
   
   private String tarMethod;
   
   private String archiveName;
   
   public CompressProcessor() {
      filePattern = FILE_PATTERN_DEFAULT;
   }

   @Override
   public void processFiles(Delivery delivery) throws ProcessorException {
      MftFolder processingIn;
      MftFolder processingOut;
      try {
         processingIn = MftFolder.createProcessingInFromDelivery(delivery);
         processingOut = MftFolder.createProcessingOutFromDelivery(delivery);
      } catch (IOException e) {
         throw new ProcessorException(
               String.format("Error while creating outbound box for delivery %s", delivery),e);
      } catch (MftPathException e) {
         throw new ProcessorException(
               String.format("Error while constructing mft path for delivery %s", delivery),e);
      }
      
      LOG.info("Compressing files ...");
      TarArchiver tarArchiver = new TarArchiver();
      tarArchiver.setCompression(TarCompressionMethod.gzip);
      File destination = new File(processingOut.getPath().toFile(), archiveName);
      tarArchiver.setDestFile(destination);
      tarArchiver.addFileSet(new DefaultFileSet(processingIn.getPath().toFile()));
      try {
         tarArchiver.createArchive();
      } catch (ArchiverException | IOException e) {
         throw new ProcessorException(
               String.format("Error while archiving %s", delivery),e);
      }
   }

   public String getFilePattern() {
      return filePattern;
   }

   public void setFilePattern(String filePattern) {
      this.filePattern = filePattern;
   }

   public String getTarMethod() {
      return tarMethod;
   }

   public void setTarMethod(String tarMethod) {
      this.tarMethod = tarMethod;
   }

   public String getArchiveName() {
      return archiveName;
   }

   public void setArchiveName(String archiveName) {
      this.archiveName = archiveName;
   }
}
