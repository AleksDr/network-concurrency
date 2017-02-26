package client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Console;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by aleks on 22.02.2017.
 */
public class ClientApplication {
  private static final Logger log = LogManager.getLogger(ClientApplication.class);
  private static final ClientConfig config = ClientConfig.getInstance();

  /**
   * Starts the client application.
   */
  private static void runApp() {
    try {
      Socket sock = new Socket(config.getHostName(), config.getPort());
      DataOutputStream dataOutStream = new DataOutputStream(sock.getOutputStream());
      Console console = System.console();
      String message = null;

      while (!config.getSafeword().equals(message)) {
        message = console.readLine();
        dataOutStream.writeUTF(message);
      }

    } catch (UnknownHostException ex) {
      log.error(ex);
    } catch (IOException ex) {
      log.error(ex);
    }
  }

  /**
   * Takes an array of arguments {String <hostname>, int <port>}.
   */
  public static void main(String[] args) {
    log.info("Started new client application");

    if (args.length > 1) {
      config.setPort(Integer.parseInt(args[1]));
    }
    if (args.length > 0) {
      config.setHostName(args[0]);
    }

    runApp();
  }

}

