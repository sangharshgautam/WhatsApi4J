package org.sangharsh.demo;

import org.springframework.stereotype.Service;

@Service
public class JmsMessageListener {
 
  public String handleMessage(final WaQueueMessage queueMessage) {
    System.out.println("JmsMessageListener Received: " + queueMessage.getText());
    return "ACK from handleMessage";
  }
}
