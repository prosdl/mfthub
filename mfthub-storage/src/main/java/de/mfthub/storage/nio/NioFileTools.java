package de.mfthub.storage.nio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.mfthub.storage.folder.MftPathBuilder;

public class NioFileTools {

   public static  Path mftPathToNIOPath(String basePath, MftPathBuilder mftPath) {
      return Paths.get(basePath, mftPath.toSegmentArray());
   }

   public static void createDirectoryIfNotExists(Path path) throws IOException {
      if (!Files.exists(path)) {
         Files.createDirectories(path);
      }
   }
}
