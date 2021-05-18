package appdata;

import entities.Airline;
import xml.AirlineHandler;
import xml.DOMBuilder;
import xml.FlightHandler;

import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AppData {
    private final AirlineData airlineData;
    private final FlightData flightData;
    private final Logger logger;

    public AppData(AirlineData airlineData, FlightData flightData) {
        this.airlineData = airlineData;
        this.flightData = flightData;
        logger = Logger.getLogger(AppData.class.getName());
    }

    public AirlineData getAirlineData() {
        return airlineData;
    }

    public FlightData getFlightData() {
        return flightData;
    }

    public void saveToFile(String airlineFilename, String flightFilename) {
        var airlinesBuilder = new DOMBuilder(new AirlineHandler(airlineData));
        var flightsBuilder = new DOMBuilder(new FlightHandler(flightData));

        airlinesBuilder.saveDataStorage(airlineFilename);
        flightsBuilder.saveDataStorage(flightFilename);
    }

    public static AppData loadFromFile(String airlineFilename, String flightFilename){
        var airlinesBuilder = new DOMBuilder(new AirlineHandler());
        var flightsBuilder = new DOMBuilder(new FlightHandler());

        airlinesBuilder.buildDataStorage(airlineFilename);
        flightsBuilder.buildDataStorage(flightFilename);

        return new AppData((AirlineData) airlinesBuilder.getDataStorage(), (FlightData) flightsBuilder.getDataStorage());
    }

}
