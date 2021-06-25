package rmi;

import entity.Nation;
import entity.Region;
import entity.WeatherRecord;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface IServerRmiTask8 extends Remote {
    public List<Nation> findAllNations() throws RemoteException;

    public Nation findNation(int id) throws RemoteException;

    public boolean createNation(Nation nation) throws RemoteException;

    public boolean updateNation(Nation nation) throws RemoteException;

    public boolean deleteNation(int id) throws RemoteException;

    public List<Region> findAllRegions() throws RemoteException;

    public Region findRegion(int id) throws RemoteException;

    public boolean createRegion(Region region) throws RemoteException;

    public boolean updateRegion(Region region) throws RemoteException;

    public boolean deleteRegion(int id) throws RemoteException;

    public List<WeatherRecord> findAllWeatherRecords() throws RemoteException;

    public WeatherRecord findWeatherRecord(int id) throws RemoteException;

    public boolean createWeatherRecord(WeatherRecord weatherRecord) throws RemoteException;

    public boolean updateWeatherRecord(WeatherRecord weatherRecord) throws RemoteException;

    public boolean deleteWeatherRecord(int id) throws RemoteException;

    public List<WeatherRecord> findWeatherRecordsByRegionID(int regionID) throws RemoteException;

    public List<WeatherRecord> findWeatherRecordsByNationLanguageForLastWeek(String language) throws RemoteException;

    public List<LocalDate> findDatesByRegionWithConditions(int regionID, int temperature) throws RemoteException;

    public double findAvgTemperatureInRegionsWithConditionsForLastWeek(double square) throws RemoteException;
}
