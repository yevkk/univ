package socket;

import entity.*;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientSocketTask8 implements AutoCloseable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public ClientSocketTask8() throws IOException{
        this.socket = new Socket("localhost", 8030);
        this.in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        this.out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Nation> findAllNations() throws IOException, ClassNotFoundException {
        out.writeObject("nations");
        out.writeObject("findAll");
        out.flush();
        return (ArrayList<Nation>) in.readObject();
    }

    public Nation findNation(int id) throws IOException, ClassNotFoundException {
        out.writeObject("nations");
        out.writeObject("find");
        out.writeInt(id);
        out.flush();
        return (Nation) in.readObject();
    }

    public boolean createNation(Nation nation) throws IOException {
        out.writeObject("nations");
        out.writeObject("create");
        out.writeObject(nation);
        out.flush();
        return in.readBoolean();
    }

    public boolean updateNation(Nation nation) throws IOException {
        out.writeObject("nations");
        out.writeObject("update");
        out.writeObject(nation);
        out.flush();
        return in.readBoolean();
    }

    public boolean deleteNation(int id) throws IOException {
        out.writeObject("nations");
        out.writeObject("delete");
        out.writeInt(id);
        out.flush();
        return in.readBoolean();
    }

    public List<Region> findAllRegions() throws IOException, ClassNotFoundException {
        out.writeObject("regions");
        out.writeObject("findAll");
        out.flush();
        return (ArrayList<Region>) in.readObject();
    }

    public Region findRegion(int id) throws IOException, ClassNotFoundException {
        out.writeObject("regions");
        out.writeObject("find");
        out.writeInt(id);
        out.flush();
        return (Region) in.readObject();
    }

    public boolean createRegion(Region region) throws IOException {
        out.writeObject("regions");
        out.writeObject("create");
        out.writeObject(region);
        out.flush();
        return in.readBoolean();
    }

    public boolean updateRegion(Region region) throws IOException {
        out.writeObject("regions");
        out.writeObject("update");
        out.writeObject(region);
        out.flush();
        return in.readBoolean();
    }

    public boolean deleteRegion(int id) throws IOException {
        out.writeObject("regions");
        out.writeObject("delete");
        out.writeInt(id);
        out.flush();
        return in.readBoolean();
    }

    public List<WeatherRecord> findAllWeatherRecords() throws IOException, ClassNotFoundException {
        out.writeObject("weather");
        out.writeObject("findAll");
        out.flush();
        return (ArrayList<WeatherRecord>) in.readObject();
    }

    public WeatherRecord findWeatherRecord(int id) throws IOException, ClassNotFoundException {
        out.writeObject("weather");
        out.writeObject("find");
        out.writeInt(id);
        out.flush();
        return (WeatherRecord) in.readObject();
    }

    public boolean createWeatherRecord(WeatherRecord weatherRecord) throws IOException {
        out.writeObject("weather");
        out.writeObject("create");
        out.writeObject(weatherRecord);
        out.flush();
        return in.readBoolean();
    }

    public boolean updateWeatherRecord(WeatherRecord weatherRecord) throws IOException {
        out.writeObject("weather");
        out.writeObject("update");
        out.writeObject(weatherRecord);
        out.flush();
        return in.readBoolean();
    }

    public boolean deleteWeatherRecord(int id) throws IOException {
        out.writeObject("weather");
        out.writeObject("delete");
        out.writeInt(id);
        out.flush();
        return in.readBoolean();
    }

    public List<WeatherRecord> findWeatherRecordsByRegionID(int regionID) throws IOException, ClassNotFoundException {
        out.writeObject("weather");
        out.writeObject("findByRegionID");
        out.writeInt(regionID);
        out.flush();
        return (ArrayList<WeatherRecord>) in.readObject();
    }

    public List<WeatherRecord> findWeatherRecordsByNationLanguageForLastWeek(String language) throws IOException, ClassNotFoundException {
        out.writeObject("weather");
        out.writeObject("findByNationLanguageForLastWeek");
        out.writeObject(language);
        out.flush();
        return (ArrayList<WeatherRecord>) in.readObject();
    }

    public List<LocalDate> findDatesByRegionWithConditions(int regionID, int temperature) throws IOException, ClassNotFoundException {
        out.writeObject("weather");
        out.writeObject("findDatesByRegionWithConditions");
        out.writeInt(regionID);
        out.writeInt(temperature);
        out.flush();
        return (ArrayList<LocalDate>) in.readObject();
    }

    public double findAvgTemperatureInRegionsWithConditionsForLastWeek(double square) throws IOException {
        out.writeObject("weather");
        out.writeObject("findAvgTempInRegionsWithConditionsForLastWeek");
        out.writeDouble(square);
        out.flush();
        return in.readDouble();
    }
}
