package appdata;

public class AppData {
    private final AirlineData airlineData;
    private final FlightData flightData;

    public AppData() {
        airlineData = new AirlineData();
        flightData = new FlightData();
    }

    public AirlineData getAirlineData() {
        return airlineData;
    }

    public FlightData getFlightData() {
        return flightData;
    }

    public void saveToFile(String filename) {
        //TODO: implement;
    }

    static AppData loadFromFile(String filename) {
        //TODO: implement;
        return null;
    }

}
