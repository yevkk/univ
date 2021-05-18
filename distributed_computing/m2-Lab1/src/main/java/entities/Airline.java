package entities;

import appdata.FlightData;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Airline {
    private int id;
    private String name;
    private String country;
    private FlightData flightData;

    public Airline(String name, String country, FlightData flightData) {
        this(-1, name, country, flightData);
    }
}
