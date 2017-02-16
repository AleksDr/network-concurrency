package edu.drobyazko.clientapp;

import java.io.Console;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientApplication {
  private static final String DEFAULT_HOSTNAME = "localhost";
  private static final String SAFEWORD = "Andromeda";
  private static final int DEFAULT_HOSTPORT = 4242;

  private static Socket socket;
  private static DataOutputStream dataOutStream;

  private static int parsePort(String str) {
    int port;

    try {
      port = Integer.parseInt(str);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(str + " is not a number");
    }

    if (port < 1 || port > 65535)
      throw new IllegalArgumentException(str + " is out of acceptable range [1,65535].");

    return port;
  }

  private static void runClient(String hostName, int port) throws IOException {
    socket = new Socket(hostName, port);
    dataOutStream = new DataOutputStream(socket.getOutputStream());
    String msg = null;
    Console console = System.console();
    while (!SAFEWORD.equals(msg)) {
      msg = console.readLine();
      dataOutStream.writeUTF(msg);
    }
    dataOutStream.close();
    socket.close();
  }

  public static void main(String[] args) {
    String hostName = DEFAULT_HOSTNAME;
    int port = DEFAULT_HOSTPORT;

    if (args.length > 1) {
      try {
        port = parsePort(args[1]);
      } catch (IllegalArgumentException ex) {
        System.out.println("The port is invalid - " + ex.getMessage() + ". Starting with default port: " + DEFAULT_HOSTPORT);
      }
    } else
      System.out.println("The host port was not specified. Starting with default port: " + DEFAULT_HOSTPORT);

    if (args.length > 0)
      hostName = args[0];
    else
      System.out.println("The host name was not specified. Starting with default name: " + DEFAULT_HOSTNAME);

    try {
      runClient(hostName, port);
    } catch (IOException ex) {
      System.out.println("Got an error: " + ex.getMessage());
    }
  }
}