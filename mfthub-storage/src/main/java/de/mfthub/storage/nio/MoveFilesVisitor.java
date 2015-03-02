package de.mfthub.storage.nio;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

public class MoveFilesVisitor
   extends SimpleFileVisitor<Path> {
   private static Logger LOG = LoggerFactory.getLogger(MoveFilesVisitor.class);

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
         // TODO move
         Files.copy(file, targetFile, StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);
         fileCount++;
         byteCount += Files.size(targetFile);
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