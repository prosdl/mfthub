package de.mfthub.core.async.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class InboundListenerDefaultImpl implements InboundListener {

   @Override
   @JmsListener(destination = MftQueues.INBOUND, containerFactory="defaultJmsListenerContainerFactory")
   public void receive(String msg) {
      System.out.println(msg);
   }

}
