package rmi;

import entity.Nation;
import entity.Region;
import entity.WeatherRecord;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDate;
import java.util.List;

public class ClientRmiTask8 {
    private final IServerRmiTask8 server;

    public ClientRmiTask8() throws RemoteException, NotBoundException {
        var registry = LocateRegistry.getRegistry(8030);
        this.server = (IServerRmiTask8) registry.lookup("rmi-server");
    }

    public List<Nation> findAllNations() throws RemoteException {
        return server.findAllNations();
    }

    public Nation findNation(int id) throws RemoteException {
        return server.findNation(id);
    }

    public boolean createNation(Nation nation) throws RemoteException {
        return server.createNation(nation);
    }

    public boolean updateNation(Nation nation) throws RemoteException {
        return server.updateNation(nation);
    }

    public boolean deleteNation(int id) throws RemoteException {
        return server.deleteNation(id);
    }

    public List<Region> findAllRegions() throws RemoteException {
        return server.findAllRegions();
    }

    public Region findRegion(int id) throws RemoteException {
        return server.findRegion(id);
    }

    public boolean createRegion(Region region) throws RemoteException {
        return server.createRegion(region);
    }

    public boolean updateRegion(Region region) throws RemoteException {
        return server.updateRegion(region);
    }

    public boolean deleteRegion(int id) throws RemoteException {
        return server.deleteRegion(id);
    }

    public List<WeatherRecord> findAllWeatherRecords() throws RemoteException {
        return server.findAllWeatherRecords();
    }

    public WeatherRecord findWeatherRecord(int id) throws RemoteException {
        return server.findWeatherRecord(id);
    }

    public boolean createWeatherRecord(WeatherRecord weatherRecord) throws RemoteException {
        return server.createWeatherRecord(weatherRecord);
    }

    public boolean updateWeatherRecord(WeatherRecord weatherRecord) throws RemoteException {
        return server.updateWeatherRecord(weatherRecord);
    }

    public boolean deleteWeatherRecord(int id) throws RemoteException {
        return server.deleteWeatherRecord(id);
    }

    public List<WeatherRecord> findWeatherRecordsByRegionID(int regionID) throws RemoteException {
        return server.findWeatherRecordsByRegionID(regionID);
    }

    public List<WeatherRecord> findWeatherRecordsByNationLanguageForLastWeek(String language) throws RemoteException {
        return server.findWeatherRecordsByNationLanguageForLastWeek(language);
    }

    public List<LocalDate> findDatesByRegionWithConditions(int regionID, int temperature) throws RemoteException {
        return server.findDatesByRegionWithConditions(regionID, temperature);
    }

    public double findAvgTemperatureInRegionsWithConditionsForLastWeek(double square) throws RemoteException {
        return server.findAvgTemperatureInRegionsWithConditionsForLastWeek(square);
    }
}
