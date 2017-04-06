package server.netUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.concurrentUtils.Channel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by aleks on 06.04.2017.
 */

public class Host implements Runnable {
  private static final Logger log = LogManager.getLogger(Host.class);
  private final MessageHandler msgHandler;
  private final Channel channel;
  private ServerSocket serverSocket;
  private Socket socket;

  public Host(int port, Channel channel, MessageHandlerFactory msgHandlerFactory){
    try {
      this.serverSocket = new ServerSocket(port);
    } catch(IOException ex){
      log.error(ex);
    }
    this.msgHandler = msgHandlerFactory.create();
    this.channel = channel;
    log.info("Created new host: " + this);
  }

  @Override
  public void run() {
    while (true) {
      try {
        socket = serverSocket.accept();
      } catch (IOException ex) {
        log.error(ex);
      }
      channel.put(new Session(socket, msgHandler));
    }
  }

  public void start() {
    new Thread(this).start();
  }
}
