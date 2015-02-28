package de.mfthub.transfer.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NioFileTools {

   public static  Path mftPathToNIOPath(String basePath, MftFolderPath mftPath) {
      return Paths.get(basePath, mftPath.toSegmentArray());
   }

   public static void createDirectoryIfNotExists(Path path) throws IOException {
      if (!Files.exists(path)) {
         Files.createDirectories(path);
      }
   }
}
