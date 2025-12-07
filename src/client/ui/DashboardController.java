package client.ui;

import client.network.RMIClient;
import client.network.TCPClient;
import client.network.UDPClient;
import common.models.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Contrôleur principal du dashboard
 */
public class DashboardController {

    private RMIClient rmiClient;
    private TCPClient tcpClient;
    private UDPClient udpClient;

    private BorderPane root;
    private VBox alertBox;
    private Label patientCountLabel;
    private Label appointmentCountLabel;
    private TableView<HealthRecord> patientTable;
    private TableView<Appointment> appointmentTable;

    private ObservableList<HealthRecord> patients;
    private ObservableList<Appointment> appointments;

    public void setClients(RMIClient rmiClient, TCPClient tcpClient, UDPClient udpClient) {
        this.rmiClient = rmiClient;
        this.tcpClient = tcpClient;
        this.udpClient = udpClient;
    }

    public void initialize() {
        patients = FXCollections.observableArrayList();
        appointments = FXCollections.observableArrayList();

        root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Barre supérieure
        root.setTop(createHeader());

        // Contenu central
        root.setCenter(createMainContent());

        // Zone d'alertes à droite
        root.setRight(createAlertPanel());

        // Charger les données initiales
        loadData();
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: linear-gradient(to right, #2196F3, #1976D2);");

        Label title = new Label("🏥 ParaCare - Système d'Assistance Paramédical");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setTextFill(Color.WHITE);

        HBox stats = new HBox(40);
        stats.setAlignment(Pos.CENTER_LEFT);

        patientCountLabel = new Label("Patients: 0");
        patientCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        patientCountLabel.setTextFill(Color.WHITE);

        appointmentCountLabel = new Label("RDV aujourd'hui: 0");
        appointmentCountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        appointmentCountLabel.setTextFill(Color.WHITE);

        Button refreshBtn = new Button("🔄 Actualiser");
        refreshBtn.setStyle("-fx-background-color: white; -fx-text-fill: #2196F3; -fx-font-weight: bold;");
        refreshBtn.setOnAction(e -> loadData());

        Button addPatientBtn = new Button("➕ Nouveau Patient");
        addPatientBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        addPatientBtn.setOnAction(e -> showAddPatientDialog());

        Button syncBtn = new Button("🔄 Sync TCP");
        syncBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        syncBtn.setOnAction(e -> syncViaTCP());

        stats.getChildren().addAll(patientCountLabel, appointmentCountLabel,
                refreshBtn, addPatientBtn, syncBtn);

        header.getChildren().addAll(title, stats);
        return header;
    }
    private Node createMainContent() {
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.6);

        // Table des patients
        VBox patientsBox = new VBox(10);
        patientsBox.setPadding(new Insets(15));

        Label patientsLabel = new Label("📋 Liste des Patients");
        patientsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        patientTable = createPatientTable();

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Rechercher un patient...");
        searchField.textProperty().addListener((obs, old, newVal) -> searchPatients(newVal));

        patientsBox.getChildren().addAll(patientsLabel, searchField, patientTable);
        VBox.setVgrow(patientTable, Priority.ALWAYS);

        // Table des rendez-vous
        VBox appointmentsBox = new VBox(10);
        appointmentsBox.setPadding(new Insets(15));

        Label appointmentsLabel = new Label("📅 Rendez-vous du Jour");
        appointmentsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        appointmentTable = createAppointmentTable();

        Button addAppointmentBtn = new Button("➕ Nouveau RDV");
        addAppointmentBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        addAppointmentBtn.setOnAction(e -> showAddAppointmentDialog());

        appointmentsBox.getChildren().addAll(appointmentsLabel, appointmentTable, addAppointmentBtn);
        VBox.setVgrow(appointmentTable, Priority.ALWAYS);

        splitPane.getItems().addAll(patientsBox, appointmentsBox);
        return splitPane;
    }

    private TableView<HealthRecord> createPatientTable() {
        TableView<HealthRecord> table = new TableView<>();
        table.setItems(patients);

        TableColumn<HealthRecord, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        idCol.setPrefWidth(80);

        TableColumn<HealthRecord, String> nameCol = new TableColumn<>("Nom Complet");
        nameCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getFullName()));
        nameCol.setPrefWidth(200);

        TableColumn<HealthRecord, Integer> ageCol = new TableColumn<>("Âge");
        ageCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getAge()));
        ageCol.setPrefWidth(60);

        TableColumn<HealthRecord, String> bloodCol = new TableColumn<>("Groupe");
        bloodCol.setCellValueFactory(new PropertyValueFactory<>("bloodType"));
        bloodCol.setPrefWidth(80);

        TableColumn<HealthRecord, String> emergencyCol = new TableColumn<>("Contact Urgence");
        emergencyCol.setCellValueFactory(new PropertyValueFactory<>("emergencyContact"));
        emergencyCol.setPrefWidth(200);

        table.getColumns().addAll(idCol, nameCol, ageCol, bloodCol, emergencyCol);

        // Double-clic pour voir les détails
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && table.getSelectionModel().getSelectedItem() != null) {
                showPatientDetails(table.getSelectionModel().getSelectedItem());
            }
        });

        return table;
    }

    private TableView<Appointment> createAppointmentTable() {
        TableView<Appointment> table = new TableView<>();
        table.setItems(appointments);

        TableColumn<Appointment, String> timeCol = new TableColumn<>("Heure");
        timeCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getAppointmentDate().toLocalTime().toString()));
        timeCol.setPrefWidth(80);

        TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        patientCol.setPrefWidth(150);

        TableColumn<Appointment, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        typeCol.setPrefWidth(120);

        TableColumn<Appointment, String> caregiverCol = new TableColumn<>("Soignant");
        caregiverCol.setCellValueFactory(new PropertyValueFactory<>("caregiverName"));
        caregiverCol.setPrefWidth(120);

        TableColumn<Appointment, String> statusCol = new TableColumn<>("Statut");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(timeCol, patientCol, typeCol, caregiverCol, statusCol);

        return table;
    }

    private VBox createAlertPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setPrefWidth(300);
        panel.setStyle("-fx-background-color: #fff3cd; -fx-border-color: #ffc107; -fx-border-width: 2;");

        Label title = new Label("🚨 Alertes & Notifications");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        alertBox = new VBox(5);
        ScrollPane scrollPane = new ScrollPane(alertBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);

        panel.getChildren().addAll(title, new Separator(), scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return panel;
    }

    public void showAlert(String message) {
        Platform.runLater(() -> {
            Label alertLabel = new Label(message);
            alertLabel.setWrapText(true);
            alertLabel.setStyle("-fx-background-color: #ffebee; -fx-padding: 10; " +
                    "-fx-border-color: #f44336; -fx-border-width: 1; " +
                    "-fx-border-radius: 5; -fx-background-radius: 5;");
            alertLabel.setFont(Font.font("Arial", 12));

            String timestamp = java.time.LocalTime.now().format(
                    DateTimeFormatter.ofPattern("HH:mm:ss"));
            Label timeLabel = new Label(timestamp);
            timeLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 10;");

            VBox alertItem = new VBox(3, timeLabel, alertLabel);
            alertBox.getChildren().add(0, alertItem);

            // Garder max 20 alertes
            if (alertBox.getChildren().size() > 20) {
                alertBox.getChildren().remove(20, alertBox.getChildren().size());
            }
        });
    }

    private void loadData() {
        new Thread(() -> {
            try {
                // Charger les patients
                List<HealthRecord> recordList = rmiClient.getAllHealthRecords();
                Platform.runLater(() -> {
                    patients.clear();
                    patients.addAll(recordList);
                    patientCountLabel.setText("Patients: " + recordList.size());
                });

                // Charger les rendez-vous du jour
                List<Appointment> aptList = rmiClient.getTodayAppointments();
                Platform.runLater(() -> {
                    appointments.clear();
                    appointments.addAll(aptList);
                    appointmentCountLabel.setText("RDV aujourd'hui: " + aptList.size());
                });

                showAlert("✅ Données chargées avec succès");

            } catch (RemoteException e) {
                Platform.runLater(() ->
                        showAlert("❌ Erreur chargement: " + e.getMessage()));
            }
        }).start();
    }

    private void searchPatients(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadData();
            return;
        }

        new Thread(() -> {
            try {
                List<HealthRecord> results = rmiClient.searchByName(query);
                Platform.runLater(() -> {
                    patients.clear();
                    patients.addAll(results);
                });
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showPatientDetails(HealthRecord record) {
        Stage detailStage = new Stage();
        detailStage.setTitle("Détails Patient - " + record.getFullName());

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: white;");

        // Informations générales
        Label headerLabel = new Label("👤 " + record.getFullName());
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        GridPane info = new GridPane();
        info.setHgap(15);
        info.setVgap(10);

        info.add(new Label("ID:"), 0, 0);
        info.add(new Label(record.getPatientId()), 1, 0);

        info.add(new Label("Âge:"), 0, 1);
        info.add(new Label(record.getAge() + " ans"), 1, 1);

        info.add(new Label("Groupe sanguin:"), 0, 2);
        info.add(new Label(record.getBloodType()), 1, 2);

        info.add(new Label("Urgence:"), 0, 3);
        info.add(new Label(record.getEmergencyContact() + " - " + record.getEmergencyPhone()), 1, 3);

        // Allergies
        if (!record.getAllergies().isEmpty()) {
            Label allergiesLabel = new Label("⚠️ Allergies:");
            allergiesLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            info.add(allergiesLabel, 0, 4);
            info.add(new Label(String.join(", ", record.getAllergies().keySet())), 1, 4);
        }

        // Traitements
        Label treatmentLabel = new Label("💊 Traitements en cours:");
        treatmentLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        ListView<String> treatmentList = new ListView<>();
        for (Treatment t : record.getTreatments()) {
            if (t.isCurrentlyActive()) {
                    treatmentList.getItems().add(t.toString());
            }
        }
        treatmentList.setPrefHeight(150);

        content.getChildren().addAll(headerLabel, new Separator(), info,
                new Separator(), treatmentLabel, treatmentList);

        Scene scene = new Scene(content, 600, 500);
        detailStage.setScene(scene);
        detailStage.show();
    }

    private void showAddPatientDialog() {
        showAlert("📝 Fonctionnalité d'ajout de patient (à implémenter en détail)");
    }

    private void showAddAppointmentDialog() {
        showAlert("📝 Fonctionnalité d'ajout de RDV (à implémenter en détail)");
    }

    private void syncViaTCP() {
        new Thread(() -> {
            boolean success = tcpClient.synchronize();
            Platform.runLater(() -> {
                if (success) {
                    showAlert("✅ Synchronisation TCP réussie");
                    loadData();
                } else {
                    showAlert("❌ Erreur de synchronisation TCP");
                }
            });
        }).start();
    }

    public BorderPane getRoot() {
        return root;
    }
}