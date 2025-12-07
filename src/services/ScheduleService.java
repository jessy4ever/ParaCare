package server.services;

import common.models.Appointment;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ScheduleService extends Remote {

    void addAppointment(Appointment appointment) throws RemoteException;

    List<Appointment> getAppointmentsFor(String patientId) throws RemoteException;

    List<Appointment> getAllAppointments() throws RemoteException;

    boolean cancelAppointment(String appointmentId) throws RemoteException;
}
