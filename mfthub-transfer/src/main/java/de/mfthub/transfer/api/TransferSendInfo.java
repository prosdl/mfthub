package de.mfthub.transfer.api;

public class TransferSendInfo {
   private long numberOfFilesSend;
   private long totalBytesSend;
   private String outboundFolder;
   
   public TransferSendInfo() {
      
   }

   public long getNumberOfFilesSend() {
      return numberOfFilesSend;
   }

   public void setNumberOfFilesSend(long numberOfFilesSend) {
      this.numberOfFilesSend = numberOfFilesSend;
   }

   public long getTotalBytesSend() {
      return totalBytesSend;
   }

   public void setTotalBytesSend(long totalBytesSend) {
      this.totalBytesSend = totalBytesSend;
   }

   public String getOutboundFolder() {
      return outboundFolder;
   }

   public void setOutboundFolder(String outboundFolder) {
      this.outboundFolder = outboundFolder;
   }
   
   
   
}
