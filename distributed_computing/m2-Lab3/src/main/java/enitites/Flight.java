package enitites;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flight extends Entity {
    private int airlineId;
    private String from;
    private String to;
    private double price;

    public Flight(int id, int airlineId, String from, String to, double price) {
        this(airlineId, from, to, price);
        setId(id);
    }
}
