package de.mfthub.transfer.api;

public class TransferReceiptInfo {

   private long numberOfFilesReceived;
   private long totalBytesReceived;
   private String inboundFolder;

   public TransferReceiptInfo() {

   }

   public long getNumberOfFilesReceived() {
      return numberOfFilesReceived;
   }

   public void setNumberOfFilesReceived(long numberOfFilesReceived) {
      this.numberOfFilesReceived = numberOfFilesReceived;
   }

   public long getTotalBytesReceived() {
      return totalBytesReceived;
   }

   public void setTotalBytesReceived(long totalBytesReceived) {
      this.totalBytesReceived = totalBytesReceived;
   }

   public String getInboundFolder() {
      return inboundFolder;
   }

   public void setInboundFolder(String inboundFolder) {
      this.inboundFolder = inboundFolder;
   }

}
