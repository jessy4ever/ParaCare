package server;
import server.rmi.HealthRecordServiceImpl;
import server.rmi.ScheduleServiceImpl;
import server.tcp.TCPServer;
import server.udp.UDPServer;
import services.HealthRecordService;
import services.ScheduleService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Serveur principal ParaCare
 * Lance tous les services : RMI, TCP et UDP
 */
public class ParaCareServer {

    private static final int RMI_PORT = 1099;

    private Registry registry;
    private HealthRecordService healthRecordService;
    private ScheduleService scheduleService;
    private TCPServer tcpServer;
    private UDPServer udpServer;

    public void start() {
        try {
            System.out.println("╔════════════════════════════════════════════╗");
            System.out.println("║   🏥 ParaCare Server - Démarrage...        ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.println();

            // 1. Démarrage du Registry RMI
            startRMIRegistry();

            // 2. Enregistrement des services RMI
            registerRMIServices();

            // 3. Démarrage du serveur TCP
            startTCPServer();

            // 4. Démarrage du serveur UDP
            startUDPServer();

            System.out.println();
            System.out.println("╔════════════════════════════════════════════╗");
            System.out.println("║   ✅ Serveur ParaCare prêt !               ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.println();
            System.out.println("Services disponibles :");
            System.out.println("  • RMI Registry      : localhost:" + RMI_PORT);
            System.out.println("  • Serveur TCP       : localhost:5000");
            System.out.println("  • Serveur UDP       : localhost:6000");
            System.out.println();
            System.out.println("Appuyez sur Ctrl+C pour arrêter le serveur");
            System.out.println();

            // Hook d'arrêt propre
            Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

            // Simulation d'alertes périodiques (pour démonstration)
            startPeriodicAlerts();

        } catch (Exception e) {
            System.err.println("❌ Erreur fatale au démarrage : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void startRMIRegistry() {
        try {
            registry = LocateRegistry.createRegistry(RMI_PORT);
            System.out.println("✅ Registry RMI créé sur le port " + RMI_PORT);
        } catch (Exception e) {
            System.err.println("❌ Erreur création registry RMI : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void registerRMIServices() {
        try {
            // Service de gestion des dossiers médicaux
            healthRecordService = new HealthRecordServiceImpl();

            registry.rebind("HealthRecordService", healthRecordService);
            System.out.println("✅ Service HealthRecordService enregistré");

            // Service de planification
            scheduleService = new ScheduleServiceImpl();

            registry.rebind("ScheduleService", scheduleService);
            System.out.println("✅ Service ScheduleService enregistré");

        } catch (Exception e) {
            System.err.println("❌ Erreur enregistrement services RMI : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void startTCPServer() {
        try {
            tcpServer = new TCPServer();
            tcpServer.start();
            System.out.println("✅ Serveur TCP démarré");
        } catch (Exception e) {
            System.err.println("❌ Erreur démarrage serveur TCP : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void startUDPServer() {
        try {
            udpServer = new UDPServer();
            udpServer.start();
            System.out.println("✅ Serveur UDP démarré");
        } catch (Exception e) {
            System.err.println("❌ Erreur démarrage serveur UDP : " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Démonstration : envoie des alertes périodiques
     */
    private void startPeriodicAlerts() {
        new Thread(() -> {
            try {
                // Attendre que tout soit démarré
                Thread.sleep(5000);

                int counter = 0;
                while (true) {
                    Thread.sleep(60000); // Toutes les minutes

                    counter++;
                    if (counter % 5 == 0) {
                        // Alerte de rappel de traitement
                        udpServer.sendTreatmentReminder(
                                "P001",
                                "Marie Dubois",
                                "Prise de Metformine"
                        );
                    }

                    if (counter % 10 == 0) {
                        // Message de maintenance
                        udpServer.broadcastAlert("Maintenance système dans 1 heure");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Arrêt propre du serveur
     */
    public void shutdown() {
        System.out.println();
        System.out.println("🛑 Arrêt du serveur ParaCare...");

        if (tcpServer != null) {
            tcpServer.shutdown();
            System.out.println("✅ Serveur TCP arrêté");
        }

        if (udpServer != null) {
            udpServer.shutdown();
            System.out.println("✅ Serveur UDP arrêté");
        }

        System.out.println("👋 Serveur ParaCare arrêté proprement");
    }

    /**
     * Point d'entrée du serveur
     */
    public static void main(String[] args) {
        ParaCareServer server = new ParaCareServer();
        server.start();

        // Garder le serveur actif
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}