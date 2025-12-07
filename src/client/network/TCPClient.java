package client.network;

import java.io.*;
import java.net.Socket;

public class TCPClient {

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public TCPClient(String host, int port) {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            throw new RuntimeException("Erreur TCP : " + e.getMessage());
        }
    }

    public String send(String message) {
        try {
            out.println(message);
            return in.readLine();
        } catch (Exception e) {
            return "Erreur : " + e.getMessage();
        }
    }

    public void disconnect() {
        try { socket.close(); } catch (Exception ignored) {}
    }
}
