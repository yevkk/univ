package server;

import dao.AppData;
import entites.Airline;
import entites.Flight;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Server implements IServer {
    public static void main(String[] args) throws RemoteException {
        var server = new Server();
        var registry = LocateRegistry.createRegistry(8030);
        var stub = UnicastRemoteObject.exportObject(server, 0);
        registry.rebind("rmi-server", stub);

        while(true) {}
    }

    private final AppData appdata = new AppData();

    @Override
    public List<Airline> findAllAirlines() throws RemoteException {
        return appdata.getAirlineDAO().findAll();
    }

    @Override
    public Airline findAirline(int id) throws RemoteException {
        return appdata.getAirlineDAO().find(id);
    }

    @Override
    public boolean createAirline(Airline airline) throws RemoteException {
        return appdata.getAirlineDAO().create(airline);
    }

    @Override
    public boolean updateAirline(Airline airline) throws RemoteException {
        return appdata.getAirlineDAO().update(airline);
    }

    @Override
    public boolean deleteAirline(int id) throws RemoteException {
        return appdata.getAirlineDAO().delete(id);
    }

    @Override
    public List<Flight> findAllFlights() throws RemoteException {
        return appdata.getFlightDAO().findAll();
    }

    @Override
    public Flight findFlight(int id) throws RemoteException {
        return appdata.getFlightDAO().find(id);
    }

    @Override
    public boolean createFlight(Flight flight) throws RemoteException {
        return appdata.getFlightDAO().create(flight);
    }

    @Override
    public boolean updateFlight(Flight flight) throws RemoteException {
        return appdata.getFlightDAO().update(flight);
    }

    @Override
    public boolean deleteFlight(int id) throws RemoteException {
        return appdata.getFlightDAO().delete(id);
    }
}
