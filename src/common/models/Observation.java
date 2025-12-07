package common.models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Observation implements Serializable {
    private int id;
    private String patientName;
    private String description;
    private LocalDateTime date;

    public Observation() {}

    public Observation(int id, String patientName, String description, LocalDateTime date) {
        this.id = id;
        this.patientName = patientName;
        this.description = description;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    @Override
    public String toString() {
        return "Observation{" +
                "id=" + id +
                ", patientName='" + patientName + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
