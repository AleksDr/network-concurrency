package server.netUtils;

import server.netUtils.MessageHandler;

/**
 * Created by aleks on 06.04.2017.
 */
public class PrintMessageHandler implements MessageHandler {
  @Override
  public void handle(String msg) {
    System.out.println(msg);
  }
}
