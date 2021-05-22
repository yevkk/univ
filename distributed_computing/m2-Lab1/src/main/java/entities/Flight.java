package entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Flight extends Entity {
    private int airlineId;
    private String departureAirport;
    private String arrivalAirport;
    private double price;

    public Flight(int id, int airlineId, String departureAirport, String arrivalAirport, double price) {
        super(id);
        this.airlineId = airlineId;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.price = price;
    }

    public Flight(int airlineId, String departureAirport, String arrivalAirport, double price) {
        this(-1, airlineId, departureAirport, arrivalAirport, price);
    }
}
