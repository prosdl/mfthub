package de.mfthub.transfer.impl.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScpToFilesVisitor extends SimpleFileVisitor<Path> {
   private static Logger LOG = LoggerFactory.getLogger(ScpToFilesVisitor.class);

   private InputStream in;
   private OutputStream out;
   private boolean rootDirVisited = false;
   private boolean dontCreateRootDir = true;

   public ScpToFilesVisitor(InputStream in, OutputStream out, boolean dontCreateRootDir) {
      this.in = in;
      this.out = out;
      this.dontCreateRootDir = dontCreateRootDir;
   }
   public ScpToFilesVisitor(InputStream in, OutputStream out) {
      this(in,out, true);
   }

   @Override
   public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
         throws IOException {
      LOG.info("scp'ing directory PRE: {}", dir.toString());

      if (dontCreateRootDir && !rootDirVisited) {
         rootDirVisited = true;
      } else {
         ScpTools.writeCommand(out, "D0755 0 " + dir.getFileName() + "\n");
         ScpTools.readAck(in);
      }
      return FileVisitResult.CONTINUE;
   }

   @Override
   public FileVisitResult postVisitDirectory(Path dir, IOException exc)
         throws IOException {
      LOG.info("scp'ing directory POST: {}", dir.toString());

      ScpTools.writeCommand(out, "E\n");
      ScpTools.readAck(in);
      return super.postVisitDirectory(dir, exc);
   }

   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
         throws IOException {
      LOG.info("scp'ing file: {}", file.toString());

      ScpTools.writeCommand(out,
            "C0644 " + Files.size(file) + " " + file.getFileName() + "\n");
      ScpTools.readAck(in);

      byte[] buffer = new byte[1024];
      try (InputStream fis = Files.newInputStream(file)) {
         while (true) {
            int len = fis.read(buffer, 0, buffer.length);
            if (len <= 0)
               break;
            out.write(buffer, 0, len);
         }
         out.flush();
      } catch (IOException e) {
         throw e;
      }

      ScpTools.writeAck(out);
      ScpTools.readAck(in);

      return FileVisitResult.CONTINUE;
   }
}
