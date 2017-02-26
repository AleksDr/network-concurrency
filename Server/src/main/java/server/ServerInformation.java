package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by aleks on 26.02.2017.
 */
public class ServerInformation {

  private static ServerInformation info;

  /**
   * Private constructor to restrict instantiating new objects.
   */
  private ServerInformation(int num) {
    numberOfConnections = num;
  }

  /**
   * Returns info singleton and instantiates new one if needed.
   */
  public static synchronized ServerInformation getInstance() {
    if (info == null) {
      log.info("Instantiating new config.");
      info = new ServerInformation(0);
    }
    return info;
  }

  private static final Logger log = LogManager.getLogger(ServerInformation.class);

  private static int numberOfConnections;

  public synchronized void increaseSessions() {
    numberOfConnections++;
  }

  public synchronized void decreaseSessions() {
    numberOfConnections--;
    if (numberOfConnections < 0)
      throw new RuntimeException("Oh, man, we got some problems here.");
  }

  public synchronized int getSessions() {
    return numberOfConnections;
  }
}
