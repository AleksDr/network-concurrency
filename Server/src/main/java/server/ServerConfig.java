package server;

import commons.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Created by aleks on 25.02.2017.
 */
public class ServerConfig implements Config {

  private static ServerConfig config;

  /**
   * Private constructor to restrict instantiating new configs.
   */
  private ServerConfig() {
  }

  /**
   * Returns config singleton and instantiates new one if needed.
   */
  public static synchronized ServerConfig getInstance() {
    if (config == null) {
      log.info("Instantiating new config.");
      config = new ServerConfig();
      config.loadFromFileUserDef();
    }
    return config;
  }

  private static InputStream inStream = null;
  private static OutputStream outStream = null;
  private static Properties properties = new Properties();
  private static final Logger log = LogManager.getLogger(ServerConfig.class);
  ClassLoader loader = Thread.currentThread().getContextClassLoader();

  private static int port;
  private static int sessionsLimit;
  private static String safeword;
  private static final String resourceName = "config.properties";

  /**
   * Generalized method to load properties from config file.
   */
  private void loadFromFile(String type) {
    try {
      inStream =  loader.getResourceAsStream(resourceName);
      properties.load(inStream);

      setPort(Integer.parseInt(properties.getProperty(type + ".port")));
      setSafeword(properties.getProperty(type + ".safeword"));
      setSessionsLimit(Integer.parseInt(properties.getProperty(type + ".sessionsLimit")));

    } catch (IOException ex) {
      log.error(ex);
    } finally {
      properties.clear();

      if (inStream != null) {
        try {
          inStream.close();
        } catch (IOException ex) {
          log.error(ex);
        }
      }
    }
  }

  /**
   * Specialized method to load default properties from config file.
   */
  @Override
  public synchronized void loadFromFileDefault() {
    loadFromFile("default");
  }

  /**
   * Specialized method to load user defined properties from config file.
   */
  @Override
  public synchronized void loadFromFileUserDef() {
    loadFromFile("user");
  }



  /**
   * Method to write user defined properties into config file.
   * DOESNT WORK RIGHT NOW. PROPERTIES WILL BE CHANGED TO PREFERENCES.
   */
  @Override
  @Deprecated
  public synchronized void writeToFileUserDef() {
    try {
      inStream = loader.getResourceAsStream(resourceName);
      outStream = new FileOutputStream(resourceName);

      properties.load(inStream);

      properties.put("user.port", String.valueOf(port));
      properties.put("user.safeword", safeword);
      properties.put("user.sessionsLimit", String.valueOf(sessionsLimit));

      properties.store(outStream, "Really cool comment");

    } catch (IOException ex) {
      log.error(ex);
    } finally {
      properties.clear();
      if (inStream != null) {
        try {
          inStream.close();
        } catch (IOException ex) {
          log.error(ex);
        }
      }
      if (outStream != null) {
        try {
          outStream.close();
        } catch (IOException ex) {
          log.error(ex);
        }
      }
    }
  }


  public synchronized int getPort() {
    return port;
  }

  public synchronized int getSessionsLimit() {
    return sessionsLimit;
  }

  public synchronized String getSafeword() {
    return safeword;
  }

  public synchronized void setPort(int port) {
    if (port < 1 || port > 65535)
      throw new IllegalArgumentException(port + "is out of allowed range.");
    this.port = port;
  }

  public synchronized void setSessionsLimit(int sessionsLimit) {
    if (sessionsLimit < 1 || sessionsLimit > 4096)
      throw new IllegalArgumentException(sessionsLimit + "is out of allowed range.");
    this.sessionsLimit = sessionsLimit;
  }

  public synchronized void setSafeword(String safeword) {
    this.safeword = safeword;
  }
}
