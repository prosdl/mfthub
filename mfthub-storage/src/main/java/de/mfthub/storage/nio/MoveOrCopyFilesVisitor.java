package de.mfthub.storage.nio;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

public class MoveOrCopyFilesVisitor extends SimpleFileVisitor<Path> {
   private static Logger LOG = LoggerFactory
         .getLogger(MoveOrCopyFilesVisitor.class);

   private AntPathMatcher antPathMatcher;
   private String pattern;
   private Path sourcePath;
   private Path targetPath;
   private boolean move = false;
   private CopyOption[] copyOptions = { StandardCopyOption.COPY_ATTRIBUTES,
         StandardCopyOption.REPLACE_EXISTING };

   private long fileCount = 0;
   private long byteCount = 0;
   private long errorCount = 0;

   public MoveOrCopyFilesVisitor(String pattern, Path sourcePath,
         Path targetPath, boolean move) {
      antPathMatcher = new AntPathMatcher();
      antPathMatcher.setCachePatterns(true);
      this.pattern = pattern;
      this.sourcePath = sourcePath;
      this.targetPath = targetPath;
      this.move = move;
   }

   public MoveOrCopyFilesVisitor(String pattern, Path sourcePath,
         Path targetPath) {
      this(pattern, sourcePath, targetPath, false);
   }

   public long getFileCount() {
      return fileCount;
   }

   public long getByteCount() {
      return byteCount;
   }

   public long getErrorCount() {
      return errorCount;
   }

   @Override
   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
         throws IOException {
      Path fileRelToSourcePath = sourcePath.relativize(file);
      LOG.trace("Found: '{}'.", fileRelToSourcePath.toString());
      if (antPathMatcher.match(pattern, fileRelToSourcePath.toString())) {
         Path targetFile = targetPath.resolve(fileRelToSourcePath);
         Files.createDirectories(targetFile.getParent());
         if (move) {
            LOG.trace("Moving '{}' to '{}'.", file.toString(),
                  targetFile.toString());
            Files.move(file, targetFile, copyOptions);
         } else {
            LOG.trace("Copying '{}' to '{}'.", file.toString(),
                  targetFile.toString());
            Files.copy(file, targetFile, copyOptions);
         }
         fileCount++;
         byteCount += Files.size(targetFile);
      }
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult visitFileFailed(Path file, IOException exc)
         throws IOException {
      LOG.warn(exc.getMessage(), exc);
      errorCount++;
      return FileVisitResult.CONTINUE;
   }

   public CopyOption[] getCopyOptions() {
      return copyOptions;
   }

   public void setCopyOptions(CopyOption... copyOptions) {
      this.copyOptions = copyOptions;
   }

   public String getPattern() {
      return pattern;
   }

   public void setPattern(String pattern) {
      this.pattern = pattern;
   }

   public Path getSourcePath() {
      return sourcePath;
   }

   public void setSourcePath(Path sourcePath) {
      this.sourcePath = sourcePath;
   }

   public Path getTargetPath() {
      return targetPath;
   }

   public void setTargetPath(Path targetPath) {
      this.targetPath = targetPath;
   }

   public boolean isMove() {
      return move;
   }

   public void setMove(boolean move) {
      this.move = move;
   }

}