package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by aleks on 25.02.2017.
 */
public class ServerApplication {

  private static final Logger log = LogManager.getLogger(ServerApplication.class);
  private static final ServerConfig config = ServerConfig.getInstance();
  private static final ServerInformation info = ServerInformation.getInstance();

  /**
   * Starts the server application.
   */
  private static void runApp() {
    try {
      ServerSocket serverSocket = new ServerSocket(config.getPort());
      log.info("Server started at localhost:" + config.getPort() + ". Sessions limit: " + config.getSessionsLimit());
      int sessionID = -1;
      while (true) {
        while (info.getSessions() < config.getSessionsLimit()) {
          Socket socket = serverSocket.accept();
          info.increaseSessions();
          try {
            new Thread(new Session(socket, ++sessionID)).start();
          } catch (Throwable ex) {
            log.error(ex);
            info.decreaseSessions();
          }
        }
        Thread.sleep(500);
      }
    } catch (InterruptedException ex) {
      log.error(ex);
    } catch (UnknownHostException ex) {
      log.error(ex);
    } catch (IOException ex) {
      log.error(ex);
    }
  }

  /**
   * Takes an array of arguments {int <port>, int <sessionsLimit>}.
   */
  public static void main(String[] args) {
    log.info("Started new server application");
    if (args.length > 0) {
      config.setPort(Integer.parseInt(args[0]));
    }
    if (args.length > 1) {
      config.setSessionsLimit(Integer.parseInt(args[1]));
    }

    runApp();
  }
}
