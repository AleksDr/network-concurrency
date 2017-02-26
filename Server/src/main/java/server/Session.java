package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by aleks on 26.02.2017.
 */
public class Session implements Runnable {

  private static final Logger log = LogManager.getLogger(Session.class);
  private final ServerConfig config = ServerConfig.getInstance();
  private final ServerInformation info = ServerInformation.getInstance();

  private final Socket sock;
  private final int sessionNumber;

  public Session(Socket sock, int sessionNumber) {
    this.sock = sock;
    this.sessionNumber = sessionNumber;
  }

  @Override
  public void run() {
    log.info("Started new session " + sessionNumber + ". There are " + info.getSessions() + " sessions.");
    try {
      DataInputStream dataInputStream = new DataInputStream(sock.getInputStream());
      String msg = dataInputStream.readUTF();
      while (!config.getSafeword().equals(msg)) {
        log.info("Session " + sessionNumber + ": " + msg);
        msg = dataInputStream.readUTF();
      }
    } catch (IOException ex) {
      log.error(sessionNumber + ": " + ex);
    }
    info.decreaseSessions();
    log.info("Closed session " + sessionNumber + ". There are " + info.getSessions() + " sessions.");
  }
}
