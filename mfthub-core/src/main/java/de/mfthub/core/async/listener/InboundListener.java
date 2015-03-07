package de.mfthub.core.async.listener;


public interface InboundListener {
   public String receive(String msg) throws Exception;
}
