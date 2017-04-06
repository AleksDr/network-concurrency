package server.netUtils;

/**
 * Created by aleks on 06.04.2017.
 */
public class PrintMessageHandlerFactory implements MessageHandlerFactory{
  @Override
  public MessageHandler create() {
    return new PrintMessageHandler();
  }
}
