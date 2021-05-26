package entity.misc;

import entity.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryType extends Entity {
    private String description;

    public DeliveryType(int id, String description) {
        this(description);
        setId(id);
    }
}
