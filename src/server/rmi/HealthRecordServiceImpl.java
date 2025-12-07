package server.rmi;

import server.services.HealthRecordService;
import common.models.HealthRecord;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class HealthRecordServiceImpl extends UnicastRemoteObject implements HealthRecordService {

    private final Map<String, HealthRecord> database = new HashMap<>();

    public HealthRecordServiceImpl() throws RemoteException {
        super();
        seedDemoData();
    }

    @Override
    public HealthRecord getRecord(String patientId) {
        return database.get(patientId);
    }

    @Override
    public List<HealthRecord> getAllRecords() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void addRecord(HealthRecord record) {
        database.put(record.getPatientId(), record);
    }

    @Override
    public void updateRecord(HealthRecord record) {
        database.put(record.getPatientId(), record);
    }

    @Override
    public boolean deleteRecord(String patientId) {
        return database.remove(patientId) != null;
    }

    private void seedDemoData() {
        HealthRecord hr = new HealthRecord(
                "P001", "Marie", "Dubois",
                java.time.LocalDate.of(1990, 4, 12)
        );
        hr.setBloodType("A+");
        hr.setGender("Femme");
        database.put("P001", hr);
    }
}
