package client.network;

import java.io.IOException;
import java.net.*;
import java.util.function.Consumer;

/**
 * Client UDP pour recevoir les alertes en temps réel
 */
public class UDPClient extends Thread {

    private String host;
    private int port;
    private DatagramSocket socket;
    private MulticastSocket multicastSocket;
    private InetAddress multicastGroup;
    private boolean running;

    private Consumer<String> alertCallback;

    private static final String MULTICAST_GROUP = "230.0.0.1";

    public UDPClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.running = false;
    }

    /**
     * Définit le callback pour les alertes reçues
     */
    public void setAlertCallback(Consumer<String> callback) {
        this.alertCallback = callback;
    }

    /**
     * Connexion au serveur UDP
     */
    public void connect() {
        try {
            socket = new DatagramSocket();

            // Enregistrement auprès du serveur
            sendMessage("REGISTER");

            // Rejoindre le groupe multicast
            multicastSocket = new MulticastSocket(port + 1);
            multicastGroup = InetAddress.getByName(MULTICAST_GROUP);
            multicastSocket.joinGroup(multicastGroup);

            // Démarrer l'écoute
            running = true;
            this.start();

            System.out.println("✅ Client UDP connecté et enregistré");

        } catch (IOException e) {
            System.err.println("❌ Erreur connexion UDP : " + e.getMessage());
        }
    }

    @Override
    public void run() {
        // Thread pour écoute unicast
        new Thread(this::listenUnicast).start();

        // Thread pour écoute multicast
        new Thread(this::listenMulticast).start();
    }

    /**
     * Écoute les messages unicast
     */
    private void listenUnicast() {
        byte[] buffer = new byte[1024];

        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                handleMessage(message);

            } catch (IOException e) {
                if (running) {
                    System.err.println("❌ Erreur réception UDP : " + e.getMessage());
                }
            }
        }
    }

    /**
     * Écoute les messages multicast (alertes broadcast)
     */
    private void listenMulticast() {
        byte[] buffer = new byte[1024];

        while (running) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                handleMessage(message);

            } catch (IOException e) {
                if (running) {
                    System.err.println("❌ Erreur réception multicast : " + e.getMessage());
                }
            }
        }
    }

    /**
     * Traite les messages reçus
     */
    private void handleMessage(String message) {
        System.out.println("📨 Message UDP reçu : " + message);

        if (message.startsWith("ALERT:")) {
            // Alerte reçue
            String alertContent = message.substring(6);
            if (alertCallback != null) {
                alertCallback.accept(alertContent);
            }

        } else if (message.startsWith("HEARTBEAT:")) {
            // Heartbeat du serveur
            System.out.println("💓 Heartbeat reçu");
            // Répondre au heartbeat
            sendMessage("HEARTBEAT");

        } else if (message.startsWith("REGISTERED:")) {
            System.out.println("✅ Enregistrement confirmé par le serveur");

        } else if (message.equals("PONG")) {
            System.out.println("🏓 Pong reçu");
        }
    }

    /**
     * Envoie un message au serveur
     */
    public void sendMessage(String message) {
        try {
            byte[] data = message.getBytes();
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);

        } catch (IOException e) {
            System.err.println("❌ Erreur envoi UDP : " + e.getMessage());
        }
    }

    /**
     * Envoie un ping au serveur
     */
    public void ping() {
        sendMessage("PING");
    }

    /**
     * Envoie une alerte au serveur (qui la diffusera)
     */
    public void sendAlert(String alertMessage) {
        sendMessage("ALERT:" + alertMessage);
    }

    /**
     * Déconnexion propre
     */
    public void disconnect() {
        running = false;

        try {
            if (socket != null && !socket.isClosed()) {
                sendMessage("UNREGISTER");
                socket.close();
            }

            if (multicastSocket != null && !multicastSocket.isClosed()) {
                multicastSocket.leaveGroup(multicastGroup);
                multicastSocket.close();
            }

            System.out.println("👋 Client UDP déconnecté");

        } catch (IOException e) {
            System.err.println("❌ Erreur déconnexion UDP : " + e.getMessage());
        }
    }

    public boolean isRunning() {
        return running;
    }
}