package appdata;

public class AppData {
    private final AirlineData airlineData;

    public AppData() {
        airlineData = new AirlineData();
    }

    public AirlineData getAirlineData() {
        return airlineData;
    }

    public void saveToFile(String filename) {
        //TODO: implement;
    }

    static AppData loadFromFile(String filename) {
        //TODO: implement;
        return null;
    }

}
