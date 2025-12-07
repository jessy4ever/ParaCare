package client.network;

import server.services.HealthRecordService;
import server.services.ScheduleService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    private final String host;
    private final int port;

    public HealthRecordService healthRecordService;
    public ScheduleService scheduleService;

    public RMIClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws Exception {
        Registry registry = LocateRegistry.getRegistry(host, port);

        healthRecordService =
                (HealthRecordService) registry.lookup("HealthRecordService");

        scheduleService =
                (ScheduleService) registry.lookup("ScheduleService");
    }
}
