package common.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Appointment implements Serializable {
    private int id;
    private String patientName;
    private LocalDateTime dateTime;
    private String doctorName;
    private String notes;

    public Appointment() {}

    public Appointment(int id, String patientName, LocalDateTime dateTime, String doctorName, String notes) {
        this.id = id;
        this.patientName = patientName;
        this.dateTime = dateTime;
        this.doctorName = doctorName;
        this.notes = notes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", dateTime=" + dateTime +
                ", doctorName='" + doctorName + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }
}
