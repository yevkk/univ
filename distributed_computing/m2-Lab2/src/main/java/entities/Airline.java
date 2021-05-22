package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Airline extends Entity {
    private String name;
    private String country;

    public Airline(int id, String name, String country) {
        this(name, country);
        setId(id);
    }
}
