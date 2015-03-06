package de.mfthub.transfer.impl.ssh;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScpFromTransfer {
   private Logger LOG = LoggerFactory.getLogger(ScpFromTransfer.class);
   private int BUFFER = 1024;

   private InputStream in;
   private OutputStream out;
   private Path localDir;

   public ScpFromTransfer(InputStream in, OutputStream out, Path localDir) {
      this.in = in;
      this.out = out;
      this.localDir = localDir;
   }

   public void transfer() throws IOException {
      LOG.info("Starting transfer");
      try (Scanner s = new Scanner(in)) {
         s.useDelimiter("\n");
         while (s.hasNextLine()) {
            String cmd = s.nextLine();
            LOG.info("cmd = {}", cmd);
            if (cmd.length() == 0) {
               throw new IOException("received empty cmd during 'scp from'.");
            }

            switch (cmd.charAt(0)) {
            case 'C':
               // Cmmmm <length> <filename>
               readFile(cmd);
               ScpTools.writeAck(out);
               break;
            case 'D':
               // Dmmmm <length> <dirname>
               readDirectory(cmd);
               ScpTools.writeAck(out);
               break;
            case 'E':
               // E
               readEndofDirectory(cmd);
               ScpTools.writeAck(out);
               break;
            case '\001':
               throw new IOException("scp-server error with code 1: "
                     + cmd.substring(1));
            case '\002':
               throw new IOException("Fatal scp-server error with code 2: "
                     + cmd.substring(1));
            default:
               throw new IOException("Unrecognized command from scp-server: "
                     + cmd);
            }
         }
      }
   }
   
   private void readDirectory(String cmd) throws IOException {
      Scanner tokenScanner = new Scanner(cmd);
      String perm = tokenScanner.next();
      long length = tokenScanner.nextLong();
      String dirname = tokenScanner.next();
      tokenScanner.close();
      LOG.info("readFile ... perm: {}, length: {}, name: '{}'", perm, length,
            dirname);
      localDir = localDir.resolve(dirname);
      Files.createDirectory(localDir);
   }
   
   private void readEndofDirectory(String cmd) {
      localDir = localDir.getParent();
   }

   private void readFile(String cmd) throws IOException {
      Scanner tokenScanner = new Scanner(cmd);
      String perm = tokenScanner.next();
      long length = tokenScanner.nextLong();
      String filename = tokenScanner.next();
      tokenScanner.close();
      LOG.info("readFile ... perm: {}, length: {}, name: '{}'", perm, length,
            filename);
      ScpTools.writeAck(out);
      fetch(localDir.resolve(filename), length);
      ScpTools.readAck(in);
   }

   private void fetch(Path localfile, long numOfRemainingBytes) throws IOException {
      try (OutputStream localOut = Files.newOutputStream(localfile)) {
         byte[] buf = new byte[BUFFER];
         while (true) {
            int bytesRead = in.read(buf, 0, (BUFFER < numOfRemainingBytes) ? BUFFER : (int) numOfRemainingBytes);
            if (bytesRead < 0) {
               throw new EOFException("Unexpected end of stream.");
            }
            localOut.write(buf, 0, bytesRead);
            numOfRemainingBytes -= bytesRead;
            if (numOfRemainingBytes == 0) {
               break;
            }
         }
         localOut.flush();
      }
   }
}
