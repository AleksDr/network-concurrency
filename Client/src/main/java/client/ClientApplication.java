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
  private static final String STOP_MSG = "Releasing control.";
  private static int port = 1337;
  private static String hostName = "localhost";

  public static void main(String[] args) {
    log.info("Started new client application");

    if (args.length > 1) {
      port = Integer.parseInt(args[1]);
    }
    if (args.length > 0) {
      hostName = args[0];
    }
    try {
      Socket sock = new Socket(hostName, port);
      DataOutputStream dataOutStream = new DataOutputStream(sock.getOutputStream());
      Console console = System.console();
      String message = null;

      while (!STOP_MSG.equals(message)) {
        message = console.readLine();
        dataOutStream.writeUTF(message);
      }

    } catch (UnknownHostException ex) {
      log.error(ex);
    } catch (IOException ex) {
      log.error(ex);
    }
  }

}

