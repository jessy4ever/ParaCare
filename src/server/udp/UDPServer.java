package server.udp;

import java.net.*;

public class UDPServer {

    private DatagramSocket socket;
    private boolean running = true;

    public void start() throws Exception {
        socket = new DatagramSocket(6000);
        System.out.println("UDP Server running on port 6000...");
    }

    public void sendTreatmentReminder(String patientId, String name, String treatment) throws Exception {
        String msg = "🔔 Rappel pour " + name + " : " + treatment;
        send(msg);
    }

    public void broadcastAlert(String message) throws Exception {
        send("⚠️ Alerte Système : " + message);
    }

    private void send(String msg) throws Exception {
        byte[] data = msg.getBytes();
        DatagramPacket packet =
                new DatagramPacket(data, data.length,
                        InetAddress.getByName("localhost"), 6001);
        socket.send(packet);
    }

    public void shutdown() {
        running = false;
        if (socket != null) socket.close();
    }
}
