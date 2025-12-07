package server.rmi;

import server.services.ScheduleService;
import common.models.Appointment;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class ScheduleServiceImpl extends UnicastRemoteObject implements ScheduleService {

    private final Map<String, Appointment> appointments = new HashMap<>();

    public ScheduleServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void addAppointment(Appointment appointment) {
        appointments.put(appointment.getAppointmentId(), appointment);
    }

    @Override
    public List<Appointment> getAppointmentsFor(String patientId) {
        List<Appointment> list = new ArrayList<>();
        for (Appointment a : appointments.values()) {
            if (a.getPatientId().equals(patientId)) list.add(a);
        }
        return list;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments.values());
    }

    @Override
    public boolean cancelAppointment(String appointmentId) {
        return appointments.remove(appointmentId) != null;
    }
}
