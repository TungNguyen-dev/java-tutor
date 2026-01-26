package tungnn.tutor.java.core.lib.net.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

  static void main() {
    int port = 8080;

    System.out.println("Server starting on port " + port + "...");

    try (ServerSocket serverSocket = new ServerSocket(port)) {

      // Wait for a client to connect
      Socket clientSocket = serverSocket.accept();
      System.out.println("Client connected: " + clientSocket.getInetAddress());

      // Read data from client
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      // Send data to client
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

      String clientMessage = in.readLine();
      System.out.println("Received from client: " + clientMessage);

      // Respond to client
      out.println("Hello from server. You said: " + clientMessage);

      // Close connection
      clientSocket.close();
      System.out.println("Client disconnected.");

    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
