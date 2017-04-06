package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import server.concurrentUtils.Channel;
import server.concurrentUtils.Dispatcher;
import server.concurrentUtils.ThreadPool;
import server.netUtils.Host;
import server.netUtils.MessageHandler;
import server.netUtils.MessageHandlerFactory;

/**
 * Created by aleks on 25.02.2017.
 */
public class ServerApplication {

  private static final Logger log = LogManager.getLogger(ServerApplication.class);
  private static int port = 1337;
  private static int sessionsLimit = 42;

  public static void main(String[] args) {
    Class msgHandlerFactoryClass;
    MessageHandlerFactory msgHandlerFactory;
    try {
      if (args.length > 0) {
        port = Integer.parseInt(args[0]);
      }
      if (args.length > 1) {
        sessionsLimit = Integer.parseInt(args[1]);
      }
      if (args.length > 2) {
        msgHandlerFactoryClass = Class.forName(args[2]);
      } else {
        msgHandlerFactoryClass = Class.forName("server.netUtils.PrintMessageHandlerFactory");
      }
      msgHandlerFactory = (MessageHandlerFactory) msgHandlerFactoryClass.newInstance();

      Channel channel = new Channel(sessionsLimit);
      Host host = new Host(port, channel, msgHandlerFactory);
      ThreadPool threadPool = new ThreadPool(sessionsLimit);
      Dispatcher dispatcher = new Dispatcher(channel, threadPool);

      host.start();
      dispatcher.start();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
      log.error(ex);
    }
  }
}
