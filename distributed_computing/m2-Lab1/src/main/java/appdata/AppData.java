package appdata;

import java.util.logging.Logger;

public class AppData {
    private final AirlineData airlineData;
    private final FlightData flightData;
    private final Logger logger;

    public AppData() {
        airlineData = new AirlineData();
        flightData = new FlightData();
        logger = Logger.getLogger(AppData.class.getName());
    }

    public AirlineData getAirlineData() {
        return airlineData;
    }

    public FlightData getFlightData() {
        return flightData;
    }

    public void saveToFile(String airlineFilename, String flightFilename) {
        //TODO: implement;
    }

    static AppData loadFromFile(String airlineFilename, String flightFilename){
        //TODO: implement;
        return null;
    }

}
