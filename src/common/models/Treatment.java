package common.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Treatment implements Serializable {
    private int id;
    private String patientName;
    private String medication;
    private String dosage;
    private LocalDate startDate;
    private LocalDate endDate;

    public Treatment() {}

    public Treatment(int id, String patientName, String medication, String dosage, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.patientName = patientName;
        this.medication = medication;
        this.dosage = dosage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return "Treatment{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", medication='" + medication + '\'' +
                ", dosage='" + dosage + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
