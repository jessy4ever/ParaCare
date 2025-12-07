package client;

import client.network.RMIClient;
import client.network.TCPClient;
import client.network.UDPClient;
import client.ui.DashboardController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Application client ParaCare avec interface JavaFX
 */
public class ParaCareClient extends Application {

    private RMIClient rmiClient;
    private TCPClient tcpClient;
    private UDPClient udpClient;
    private DashboardController dashboardController;

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("🚀 Démarrage du client ParaCare...");

            // Connexion aux services réseau
            connectToServers();

            // Chargement de l'interface graphique
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 800);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            // Récupération du contrôleur
            dashboardController = loader.getController();
            dashboardController.setClients(rmiClient, tcpClient, udpClient);
            dashboardController.initialize();

            // Configuration de la fenêtre
            primaryStage.setTitle("ParaCare - Système d'Assistance Paramédical");
            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.show();

            System.out.println("✅ Interface graphique chargée");

            // Gestion de la fermeture
            primaryStage.setOnCloseRequest(event -> {
                shutdown();
                Platform.exit();
            });

        } catch (IOException e) {
            showError("Erreur de démarrage",
                    "Impossible de charger l'interface : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Connexion aux différents serveurs
     */
    private void connectToServers() {
        try {
            // Connexion RMI

            rmiClient = new RMIClient("localhost", 1099);
            rmiClient.connect();
            System.out.println("✅ Connecté au serveur RMI");

            // Connexion TCP

            tcpClient = new TCPClient("localhost", 5000);
            System.out.println("✅ Client TCP initialisé");

            // Connexion UDP avec callback pour les alertes
            udpClient = new UDPClient("localhost", 6000);
            udpClient.setAlertCallback(this::handleAlert);
            udpClient.connect();
            System.out.println("✅ Connecté au serveur UDP");

        } catch (Exception e) {
            showError("Erreur de connexion",
                    "Impossible de se connecter aux serveurs : " + e.getMessage());
            System.err.println("❌ Erreur connexion : " + e.getMessage());
        }
    }

    /**
     * Gestion des alertes UDP
     */
    private void handleAlert(String alertMessage) {
        Platform.runLater(() -> {
            if (dashboardController != null) {
                dashboardController.showAlert(alertMessage);
            }
            System.out.println("🚨 Alerte reçue : " + alertMessage);
        });
    }

    /**
     * Affiche une erreur
     */
    private void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Arrêt propre de l'application
     */
    private void shutdown() {
        System.out.println("🛑 Arrêt du client ParaCare...");

        if (udpClient != null) {
            udpClient.disconnect();
        }

        if (tcpClient != null) {
            tcpClient.disconnect();
        }

        System.out.println("👋 Client arrêté proprement");
    }

    @Override
    public void stop() {
        shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}