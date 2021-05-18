package items;

import java.time.ZonedDateTime;

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

    public int getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(int airlineId) {
        this.airlineId = airlineId;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ZonedDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(ZonedDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }
}
