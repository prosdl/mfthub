package de.mfthub.transfer.api;

public class TransferSendInfo extends TransferInfo {
   private String outboundFolder;
   
   public TransferSendInfo() {
      
   }

   public String getOutboundFolder() {
      return outboundFolder;
   }

   public void setOutboundFolder(String outboundFolder) {
      this.outboundFolder = outboundFolder;
   }
}
