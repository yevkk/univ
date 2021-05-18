package entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class Flight {
    private int id;
    private int airlineId;
    private String departureAirport;
    private String arrivalAirport;
    private double price;
    private ZonedDateTime departureDateTime;

    public Flight(int airlineId, String departureAirport, String arrivalAirport, double price, ZonedDateTime departureDateTime) {
        this(-1, airlineId, departureAirport, arrivalAirport, price, departureDateTime);
    }
}
