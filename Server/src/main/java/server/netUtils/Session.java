package server.netUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by aleks on 06.04.2017.
 */
public class Session implements Runnable {
  private static final Logger log = LogManager.getLogger(Session.class);
  private final Socket socket;
  private final MessageHandler msgHandler;

  private final static String DISCONNECT_MSG = "Releasing control.";

  public Session(Socket socket, MessageHandler msgHandler) {
    this.msgHandler = msgHandler;
    this.socket = socket;
    log.info("Created new session: " + this);
  }

  @Override
  public void run() {
    try {
      DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
      String msg = "";
      while (42 == 42) {
        msg = dataInputStream.readUTF();
        msgHandler.handle("Message from " + Thread.currentThread().getId() + ": " + msg);
        if (DISCONNECT_MSG.equals(msg)) {
          msgHandler.handle("Client disconnected: " + Thread.currentThread().getId());
          break;
        }
      }
    } catch (IOException ex) {
      log.error(ex);
    }
  }
}
