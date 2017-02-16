package edu.drobyazko.serverapp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;

public class ServerApplication {

  private static final int DEFAULT_PORT = 4242;
  private static final String SAFEWORD = "Andromeda";

  private static ServerSocket servSocket;
  private static Socket socket;
  private static DataInputStream dataInStream;

  private static int parsePort(String str) throws IllegalArgumentException {
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

  private static void runServer(int port) throws IOException {
    servSocket = new ServerSocket(port);
    socket = servSocket.accept();
    dataInStream = new DataInputStream(socket.getInputStream());
    String msg = null;
    while (!SAFEWORD.equals(msg)) {
      msg = dataInStream.readUTF();
      System.out.println("Got message: " + msg);
    }
    System.out.println("You have failed. We must find another way. Releasing control.");
    dataInStream.close();
    socket.close();
    servSocket.close();
  }

  public static void main(String[] args) {
    int port = DEFAULT_PORT;

    if (args.length == 0) {
      System.out.println("The port was not specified. Starting with default port: " + DEFAULT_PORT);
    } else if (args.length >= 1) {
      try {
        port = parsePort(args[0]);
      } catch (IllegalArgumentException ex) {
        System.out.println("The port is invalid - " + ex.getMessage() + ". Starting with default port: " + DEFAULT_PORT);
      }
    }

    try {
      runServer(port);
    } catch (IOException ex) {
      System.out.println("Got an error: " + ex.getMessage());
    }
  }
}