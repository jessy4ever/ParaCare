package server.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {

    private ServerSocket serverSocket;
    private boolean running = true;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("TCP Server listening on port 5000...");

            while (running) {
                Socket client = serverSocket.accept();
                new ClientHandler(client).start();
            }

        } catch (IOException e) {
            if (running) e.printStackTrace();
        }
    }

    public void shutdown() {
        running = false;
        try {
            serverSocket.close();
        } catch (Exception ignored) {}
    }

    private static class ClientHandler extends Thread {

        private final Socket socket;

        ClientHandler(Socket socket) { this.socket = socket; }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                String line;
                while ((line = in.readLine()) != null) {
                    out.println("SERVER RESPONSE: " + line.toUpperCase());
                }
            } catch (Exception ignored) {}
        }
    }
}
