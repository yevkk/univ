package rmi;

import dao.*;
import entity.*;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.List;

public class ServerRmiTask8 implements IServerRmiTask8 {
    public static void main(String[] args) throws RemoteException {
        var server = new ServerRmiTask8();
        var registry = LocateRegistry.createRegistry(8030);
        Remote stub = UnicastRemoteObject.exportObject(server, 0);
        registry.rebind("rmi-server", stub);

        while(true) {}
    }

    private final NationDAO nationDAO;
    private final RegionDAO regionDAO;
    private final WeatherRecordDAO weatherDAO;

    public ServerRmiTask8() {
        this.nationDAO = new NationDAO();
        this.regionDAO = new RegionDAO();
        this.weatherDAO = new WeatherRecordDAO();
    }

    @Override
    public List<Nation> findAllNations() throws RemoteException {
        return nationDAO.findAll();
    }

    @Override
    public Nation findNation(int id) throws RemoteException {
        return nationDAO.find(id);
    }

    @Override
    public boolean createNation(Nation nation) throws RemoteException {
        return nationDAO.create(nation);
    }

    @Override
    public boolean updateNation(Nation nation) throws RemoteException {
        return nationDAO.update(nation);
    }

    @Override
    public boolean deleteNation(int id) throws RemoteException {
        return nationDAO.delete(id);
    }

    @Override
    public List<Region> findAllRegions() throws RemoteException {
        return regionDAO.findAll();
    }

    @Override
    public Region findRegion(int id) throws RemoteException {
        return regionDAO.find(id);
    }

    @Override
    public boolean createRegion(Region region) throws RemoteException {
        return regionDAO.create(region);
    }

    @Override
    public boolean updateRegion(Region region) throws RemoteException {
        return regionDAO.update(region);
    }

    @Override
    public boolean deleteRegion(int id) throws RemoteException {
        return regionDAO.delete(id);
    }

    @Override
    public List<WeatherRecord> findAllWeatherRecords() throws RemoteException {
        return weatherDAO.findAll();
    }

    @Override
    public WeatherRecord findWeatherRecord(int id) throws RemoteException {
        return weatherDAO.find(id);
    }

    @Override
    public boolean createWeatherRecord(WeatherRecord weatherRecord) throws RemoteException {
        return weatherDAO.create(weatherRecord);
    }

    @Override
    public boolean updateWeatherRecord(WeatherRecord weatherRecord) throws RemoteException {
        return weatherDAO.update(weatherRecord);
    }

    @Override
    public boolean deleteWeatherRecord(int id) throws RemoteException {
        return weatherDAO.delete(id);
    }

    @Override
    public List<WeatherRecord> findWeatherRecordsByRegionID(int regionID) throws RemoteException {
        return weatherDAO.findByRegion(regionID);
    }

    @Override
    public List<WeatherRecord> findWeatherRecordsByNationLanguageForLastWeek(String language) throws RemoteException {
        return weatherDAO.findByNationLanguageForLastWeek(language);
    }

    @Override
    public List<LocalDate> findDatesByRegionWithConditions(int regionID, int temperature) throws RemoteException {
        return weatherDAO.findDatesByRegionWithConditions(regionID, temperature);
    }

    @Override
    public double findAvgTemperatureInRegionsWithConditionsForLastWeek(double square) throws RemoteException {
        return weatherDAO.findAverageTemperatureInRegionsWithConditionsForLastWeek(square);
    }
}
