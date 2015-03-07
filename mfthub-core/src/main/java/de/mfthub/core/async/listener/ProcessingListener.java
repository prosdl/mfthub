package de.mfthub.core.async.listener;


public interface ProcessingListener {
   public String receive(String msg) throws Exception;
}
