package de.mfthub.transfer.impl.ssh;

import com.jcraft.jsch.UserInfo;

public class ScpSimplePasswordUserInfo implements UserInfo {

   private String password;

   public ScpSimplePasswordUserInfo(String password) {
      this.password = password;
   }

   @Override
   public void showMessage(String arg0) {
   }

   @Override
   public boolean promptYesNo(String arg0) {
      return true;
   }

   @Override
   public boolean promptPassword(String arg0) {
      return true;
   }

   @Override
   public boolean promptPassphrase(String arg0) {
      return true;
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getPassphrase() {
      return null;
   }
}
