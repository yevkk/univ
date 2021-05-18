package entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Airline extends Entity {
    private String name;
    private String country;

    public Airline(int id, String name, String country) {
        super(id);
        this.name = name;
        this.country = country;
    }

    public Airline(String name, String country) {
        this(-1, name, country);
    }

}
