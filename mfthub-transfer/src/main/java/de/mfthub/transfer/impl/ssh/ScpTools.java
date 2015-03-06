package de.mfthub.transfer.impl.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ScpTools {
   
   static public void writeCommand(OutputStream out, String cmd) throws IOException {
      out.write(cmd.getBytes());
      out.flush();
   }
   static public void writeAck(OutputStream out) throws IOException {
      out.write(0);
      out.flush();
   }

   static public void readAck(InputStream in) throws IOException {
      int b;
      StringBuffer sb;
      try {
         b = in.read();

         if (b == 0)
            return;

         if (b == -1) {
            throw new IOException("ssh: no server response");
         }

         sb = new StringBuffer();

         int c = in.read();
         while (c > 0 && c != '\n') {
            sb.append((char) c);
            c = in.read();
         }
      } catch (IOException e) {
         throw new IOException("Error while reading ack.", e);
      }

      switch (b) {
      case 1:
         throw new IOException("server error: " + sb.toString());
      case 2:
         throw new IOException("fatal server error: " + sb.toString());
      default:
         throw new IOException("unknown code: " + b + ", response: "
               + sb.toString());

      }

   }

}
