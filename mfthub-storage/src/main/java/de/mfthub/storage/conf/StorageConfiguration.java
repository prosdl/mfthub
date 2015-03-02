package de.mfthub.storage.conf;

public enum StorageConfiguration {
   INSTANCE;
   
   private String storageRootDirectory = null;
   
   public void initialize(String storageRootDirectory) {
      if (this.storageRootDirectory != null) {
         throw new IllegalStateException("Storage was already initialized");
      }
      this.storageRootDirectory = storageRootDirectory;
   }
   
   public String getStorageRootDirectory() {
      if (storageRootDirectory == null) {
         throw new IllegalStateException("Storage not initialized");
      }
      return storageRootDirectory;
   }

}
