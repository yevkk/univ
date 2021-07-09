package entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Entity implements Serializable, Cloneable {
    private int id;

    public Entity() {
        this(-1);
    }
}
