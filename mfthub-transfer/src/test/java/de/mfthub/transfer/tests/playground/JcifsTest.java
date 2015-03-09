package de.mfthub.transfer.tests.playground;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.io.ByteStreams;

@SpringApplicationConfiguration
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class JcifsTest {
   
   @Configuration
   static class Conf {
      
   }
   
   @Test
   public void simple() throws FileNotFoundException, IOException {
      NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("scptest:scptest");
//      SmbFile file = new SmbFile("smb://DELL/Users/Peter/test.txt", auth);
      SmbFile file = new SmbFile("smb://mint-vm/ourfiles/test.txt", auth);
      File local = new File("/tmp/newtest.txt");
      try (OutputStream out = new FileOutputStream(local); InputStream in = file.getInputStream()) {
         ByteStreams.copy(in, out);
      } catch (Exception e) {
         throw e;
      }  
   }
}
