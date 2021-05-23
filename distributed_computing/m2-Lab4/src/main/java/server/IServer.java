package server;

import entites.Airline;
import entites.Flight;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServer extends Remote {
    public List<Airline> findAllAirlines() throws RemoteException;

    public Airline findAirline(int id) throws RemoteException;

    public boolean createAirline(Airline airline) throws RemoteException;

    public boolean updateAirline(Airline airline) throws RemoteException;

    public boolean deleteAirline(int id) throws RemoteException;

    public List<Flight> findAllFlights() throws RemoteException;

    public Flight findFlight(int id) throws RemoteException;

    public boolean createFlight(Flight flight) throws RemoteException;

    public boolean updateFlight(Flight flight) throws RemoteException;

    public boolean deleteFlight(int id) throws RemoteException;
}
