package tungnn.tutor.java.core.lib.net.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleClient {

  static void main() {
    String host = "localhost";
    int port = 8080;

    System.out.println("Connecting to server " + host + ":" + port + "...");

    try (Socket socket = new Socket(host, port)) {

      // Send data to server
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

      // Read data from server
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      String message = "Hello Server";
      out.println(message);
      System.out.println("Sent to server: " + message);

      String response = in.readLine();
      System.out.println("Received from server: " + response);

    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
