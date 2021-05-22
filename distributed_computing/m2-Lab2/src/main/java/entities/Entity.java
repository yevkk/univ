package entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Entity {
    private int id;

    public Entity() {
        this(-1);
    }
}
