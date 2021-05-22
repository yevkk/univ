package client;

import entites.Airline;
import entites.Flight;
import server.IServer;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;



public class Client {
    private final IServer server;

    public Client() throws RemoteException, NotBoundException {
        var registry = LocateRegistry.getRegistry(8030);
        this.server = (IServer) registry.lookup("rmi-server");
    }


    public List<Airline> findAllAirlines() throws RemoteException {
        return server.findAllAirlines();
    }

    public Airline findAirline(int id) throws RemoteException {
        return server.findAirline(id);
    }

    public boolean createAirline(Airline airline) throws RemoteException {
        return server.createAirline(airline);
    }

    public boolean updateAirline(Airline airline) throws RemoteException {
        return server.updateAirline(airline);
    }

    public boolean deleteAirline(int id) throws RemoteException {
        return server.deleteAirline(id);
    }

    public List<Flight> findAllFlights() throws RemoteException {
        return server.findAllFlights();
    }

    public Flight findFlight(int id) throws RemoteException {
        return server.findFlight(id);
    }

    public boolean createFlight(Flight flight) throws RemoteException {
        return server.createFlight(flight);
    }

    public boolean updateFlight(Flight flight) throws RemoteException {
        return server.updateFlight(flight);
    }

    public boolean deleteFlight(int id) throws RemoteException {
        return server.deleteFlight(id);
    }

}
