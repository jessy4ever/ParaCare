package common.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Modèle principal représentant un dossier médical patient
 * Objet complexe avec collections imbriquées pour démontrer RMI
 */
public class HealthRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    // Informations patient
    private String patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String bloodType;
    private String gender;

    // Collections complexes

    private List<Treatment> treatments;
    private List<Observation> observations;
    private Map<String, String> allergies; // allergie -> sévérité
    private List<String> chronicDiseases;
    private Map<LocalDate, String> notes; // notes indexées par date

    // Contacts
    private String emergencyContact;
    private String emergencyPhone;
    private String primaryDoctor;

    // Métadonnées
    private LocalDate createdDate;
    private LocalDate lastModified;
    private String lastModifiedBy;

    public HealthRecord() {
        this.treatments = new ArrayList<>();
        this.observations = new ArrayList<>();
        this.allergies = new HashMap<>();
        this.chronicDiseases = new ArrayList<>();
        this.notes = new TreeMap<>(); // Tri automatique par date
        this.createdDate = LocalDate.now();
        this.lastModified = LocalDate.now();
    }

    public HealthRecord(String patientId, String firstName, String lastName, LocalDate dateOfBirth) {
        this();
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    // Méthodes utilitaires
    public void addTreatment(Treatment treatment) {
        this.treatments.add(treatment);
        this.lastModified = LocalDate.now();
    }

    public void addObservation(Observation observation) {
        this.observations.add(observation);
        this.lastModified = LocalDate.now();
    }

    public void addAllergy(String allergen, String severity) {
        this.allergies.put(allergen, severity);
        this.lastModified = LocalDate.now();
    }

    public void addNote(String note) {
        this.notes.put(LocalDate.now(), note);
        this.lastModified = LocalDate.now();
    }

    public int getAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // Getters et Setters
    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.lastModified = LocalDate.now();
    }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.lastModified = LocalDate.now();
    }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public List<Treatment> getTreatments() { return treatments; }
    public void setTreatments(List<Treatment> treatments) { this.treatments = treatments; }

    public List<Observation> getObservations() { return observations; }
    public void setObservations(List<Observation> observations) { this.observations = observations; }

    public Map<String, String> getAllergies() { return allergies; }
    public void setAllergies(Map<String, String> allergies) { this.allergies = allergies; }

    public List<String> getChronicDiseases() { return chronicDiseases; }
    public void setChronicDiseases(List<String> chronicDiseases) { this.chronicDiseases = chronicDiseases; }

    public Map<LocalDate, String> getNotes() { return notes; }
    public void setNotes(Map<LocalDate, String> notes) { this.notes = notes; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getEmergencyPhone() { return emergencyPhone; }
    public void setEmergencyPhone(String emergencyPhone) { this.emergencyPhone = emergencyPhone; }

    public String getPrimaryDoctor() { return primaryDoctor; }
    public void setPrimaryDoctor(String primaryDoctor) { this.primaryDoctor = primaryDoctor; }

    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    public LocalDate getLastModified() { return lastModified; }
    public void setLastModified(LocalDate lastModified) { this.lastModified = lastModified; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }

    @Override
    public String toString() {
        return String.format("HealthRecord[%s - %s %s, Age: %d]",
                patientId, firstName, lastName, getAge());
    }
}