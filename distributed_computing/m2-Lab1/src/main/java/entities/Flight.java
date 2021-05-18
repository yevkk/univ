package entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class Flight extends Entity {
    private int airlineId;
    private String departureAirport;
    private String arrivalAirport;
    private double price;
    private ZonedDateTime departureDateTime;

    public Flight(int id, int airlineId, String departureAirport, String arrivalAirport, double price, ZonedDateTime departureDateTime) {
        super(id);
        this.airlineId = airlineId;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.price = price;
        this.departureDateTime = departureDateTime;
    }

    public Flight(int airlineId, String departureAirport, String arrivalAirport, double price, ZonedDateTime departureDateTime) {
        this(-1, airlineId, departureAirport, arrivalAirport, price, departureDateTime);
    }
}
