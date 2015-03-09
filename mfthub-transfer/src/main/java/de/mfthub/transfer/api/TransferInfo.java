package de.mfthub.transfer.api;

public abstract class TransferInfo {
   protected long numberOfFiles;
   protected long totalBytes;
   protected int timeNeededInMilliSecs;
   
   
   public long getNumberOfFiles() {
      return numberOfFiles;
   }
   public void setNumberOfFiles(long numberOfFiles) {
      this.numberOfFiles = numberOfFiles;
   }
   public long getTotalBytes() {
      return totalBytes;
   }
   public void setTotalBytes(long totalBytes) {
      this.totalBytes = totalBytes;
   }
   public int getTimeNeededInMilliSecs() {
      return timeNeededInMilliSecs;
   }
   public void setTimeNeededInMilliSecs(int timeNeededInMilliSecs) {
      this.timeNeededInMilliSecs = timeNeededInMilliSecs;
   }
   
   public double getTransferSpeedInKBperSec() {
      if (timeNeededInMilliSecs > 0) {
         return ((double) totalBytes) / timeNeededInMilliSecs * 1000.0 / 1024.0; 
      } else {
         return Double.NaN;
      }
   }
   public double getTransferSpeedInMBperSec() {
      if (timeNeededInMilliSecs > 0) {
         return ((double) totalBytes) / timeNeededInMilliSecs * 1000.0 / 1024.0 / 1024.0; 
      } else {
         return Double.NaN;
      }
   }
   
}
