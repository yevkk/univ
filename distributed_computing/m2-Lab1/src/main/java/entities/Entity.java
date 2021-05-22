package entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public abstract class Entity implements Cloneable, Serializable {
    private int id;

    public Entity() {
        this(-1);
    }
}
