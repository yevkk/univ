package dao;

import lombok.Getter;

@Getter
public class AppData {
    private final AirlineDAO airlineDAO = new AirlineDAO();
    private final FlightDAO flightDAO = new FlightDAO();
}
