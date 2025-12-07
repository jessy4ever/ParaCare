package server.services;

import common.models.HealthRecord;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface HealthRecordService extends Remote {

    HealthRecord getRecord(String patientId) throws RemoteException;

    List<HealthRecord> getAllRecords() throws RemoteException;

    void addRecord(HealthRecord record) throws RemoteException;

    void updateRecord(HealthRecord record) throws RemoteException;

    boolean deleteRecord(String patientId) throws RemoteException;
}
