package de.mfthub.transfer.api;

public class TransferReceiptInfo extends TransferInfo {

   private String inboundFolder;

   public TransferReceiptInfo() {

   }

   public String getInboundFolder() {
      return inboundFolder;
   }

   public void setInboundFolder(String inboundFolder) {
      this.inboundFolder = inboundFolder;
   }

}
