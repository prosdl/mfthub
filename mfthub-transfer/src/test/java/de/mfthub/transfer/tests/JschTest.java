package de.mfthub.transfer.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Logger;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import de.mfthub.transfer.exception.TransmissionException;
import de.mfthub.transfer.impl.ssh.ScpFromTransfer;
import de.mfthub.transfer.impl.ssh.ScpSimplePasswordUserInfo;
import de.mfthub.transfer.impl.ssh.ScpToFilesVisitor;
import de.mfthub.transfer.impl.ssh.ScpTools;

@SpringApplicationConfiguration
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class JschTest {
   
   
   @Configuration
   static class Conf {
      
   }
   
   static final String SOURCE_MODE = "-f";
   static final String SINK_MODE = "-t";
   static final String SCP = "scp";

   UserInfo userInfo = new ScpSimplePasswordUserInfo("scptest");
   
   public JschTest() {
      JSch.setLogger(new Logger() {
         
         @Override
         public void log(int arg0, String arg1) {
            System.out.println(arg1);
         }
         
         @Override
         public boolean isEnabled(int arg0) {
            return true;
         }
      });
   }
  
   @Test
   public void testCopySingleFileToSSH() throws JSchException, IOException, TransmissionException {

      File file = new File("/tmp/file_to_send");

      JSch jsch = new JSch();
      Session session = jsch.getSession("scptest", "localhost", 22);
      session.setUserInfo(userInfo);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      String cmd = SCP + " " + SINK_MODE + " " + "/home/scptest/receive/incoming_file";
      ChannelExec channel = (ChannelExec) session.openChannel("exec");
      channel.setCommand(cmd);

      OutputStream out = channel.getOutputStream();
      InputStream in = channel.getInputStream();

      channel.connect();
      ScpTools.readAck(in);

      cmd = "C0644 " + file.length() + " " + file.getName() + "\n";
      out.write(cmd.getBytes());
      out.flush();
      ScpTools.readAck(in);

      byte[] buffer = new byte[1024];
      try (FileInputStream fis = new FileInputStream(file)) {
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
      
      out.close();

      channel.disconnect();
      session.disconnect();
   }
   
   
   @Test
   public void testCopyMultipleFilesToSSH() throws JSchException, IOException, TransmissionException {

      File file = new File("/tmp/directory_to_send");

      JSch jsch = new JSch();
      Session session = jsch.getSession("scptest", "localhost", 22);
      session.setUserInfo(userInfo);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      String cmd = "scp -r -d -t /home/scptest/receive/dir_in";
      ChannelExec channel = (ChannelExec) session.openChannel("exec");
      channel.setCommand(cmd);

      OutputStream out = channel.getOutputStream();
      InputStream in = channel.getInputStream();

      channel.connect();
      ScpTools.readAck(in);
      
      Files.walkFileTree(Paths.get(file.getPath()), new ScpToFilesVisitor(in, out));
      
      out.close();

      channel.disconnect();
      session.disconnect();
   }
   
   @Test
   public void testReceiveMultipleFilesFromSSH() throws JSchException, IOException {
      JSch jsch = new JSch();
      Session session = jsch.getSession("scptest", "localhost", 22);
      session.setUserInfo(userInfo);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect();

      String cmd = "scp -r -f /home/scptest/receive/*";
      ChannelExec channel = (ChannelExec) session.openChannel("exec");
      channel.setCommand(cmd);
      
      OutputStream out = channel.getOutputStream();
      InputStream in = channel.getInputStream();

      channel.connect();
      ScpTools.writeAck(out);
      
      new ScpFromTransfer(in, out, Paths.get("/tmp/from_scp")).transfer();
      
      channel.disconnect();
      session.disconnect();
      
      
   }
}
