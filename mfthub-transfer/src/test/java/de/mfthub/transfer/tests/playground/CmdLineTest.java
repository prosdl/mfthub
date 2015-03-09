package de.mfthub.transfer.tests.playground;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringApplicationConfiguration
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class CmdLineTest {
   @Configuration
   static class Conf {
      
   }
   
   @Test
   public void testCp() {
      String executable = "/tmp/foo.sh";
      if (!Files.isExecutable(Paths.get(executable))) {
         throw new RuntimeException("can't exec " + executable);
      }
      String cmdLine =  executable + " ${options} arg1 'arg2' \"arg3\" ${arg4}";
      CommandLine cl = CommandLine.parse(cmdLine);
      DefaultExecutor exec = new DefaultExecutor();
      Map<String, String> map = new HashMap<>();
      map.put("options", "/p");
      map.put("arg4", "blabla");
      cl.setSubstitutionMap(map);
      ExecuteWatchdog dog = new ExecuteWatchdog(5000);
      exec.setWatchdog(dog);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ByteArrayOutputStream err = new ByteArrayOutputStream();
      PumpStreamHandler streamHandler = new PumpStreamHandler(out, err);
      exec.setStreamHandler(streamHandler);
      
      long t0 = System.currentTimeMillis();
      try {
         exec.execute(cl);
      } catch (ExecuteException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      long t1 = System.currentTimeMillis();
      System.out.println("exec finished after: " + (t1-t0) + " milli secs.");
      
      System.out.println("OUT\n-->" + out.toString() + "<--");
      System.out.println("\n\nERR\n-->" + err.toString()+ "<--");
   }
}
