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

/**
 * Recursively copies files from local to remote via Jsch/scp.
 * 
 * References:
 * 
 * <ul>
 * <li>https://blogs.oracle.com/janp/entry/how_the_scp_protocol_works</li>
 * <li>https://svn.apache.org/repos/asf/ant/core/tags/ANT_194/src/main/org/apache/tools/ant/taskdefs/optional/ssh/ScpToMessage.java</li>
 * </ul>
 * 
 * @author prosdl
 *
 */
public class ScpToFilesVisitor extends SimpleFileVisitor<Path> {
   private static Logger LOG = LoggerFactory.getLogger(ScpToFilesVisitor.class);
   private static int BUFFER_SIZE = 1024;

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

      byte[] buffer = new byte[BUFFER_SIZE];
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
