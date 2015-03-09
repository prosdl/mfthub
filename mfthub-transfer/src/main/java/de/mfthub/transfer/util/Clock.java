package de.mfthub.transfer.util;

public class Clock {
   
   private long start;
   private long stop;
   
   public Clock() {
      reset();
   }
   
   public void stop() {
      if (stop >= 0) {
         throw new IllegalStateException("clock already stopped");
      }
      stop = System.currentTimeMillis();
   }
   public int stopAndReturnPassedTime() {
      stop();
      return passedTime();
   }
   
   public int passedTime() {
      if (stop < 0) {
         throw new IllegalStateException("clock not stopped.");
      }
      return (int) (stop - start);
   }
   
   public void reset() {
      start = System.currentTimeMillis();
      stop = -1;
   }
}
