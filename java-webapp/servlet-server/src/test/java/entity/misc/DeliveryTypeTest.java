package entity.misc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeliveryTypeTest {
    @Test
    public void emptyConstructor() {
        var delType = new DeliveryType();

        assertEquals(-1, delType.getId());
        assertNull(delType.getDescription());
    }

    @Test
    public void constructor() {
        var delType1 = new DeliveryType(1, "desc1");
        var delType2 = new DeliveryType(2, "desc2");

        assertEquals(1, delType1.getId());
        assertEquals("desc1", delType1.getDescription());

        assertEquals(2, delType2.getId());
        assertEquals("desc2", delType2.getDescription());
    }

    @Test
    public void constructorAllArgsLombok() {
        var delType1 = new DeliveryType("desc1");
        var delType2 = new DeliveryType("desc2");

        assertEquals(-1, delType1.getId());
        assertEquals("desc1", delType1.getDescription());

        assertEquals(-1, delType2.getId());
        assertEquals("desc2", delType2.getDescription());
    }

    @Test
    public void setters() {
        var delType1 = new DeliveryType();
        delType1.setId(1);
        delType1.setDescription("desc1");

        var delType2 = new DeliveryType();
        delType2.setId(2);
        delType2.setDescription("desc2");

        assertEquals(1, delType1.getId());
        assertEquals("desc1", delType1.getDescription());

        assertEquals(2, delType2.getId());
        assertEquals("desc2", delType2.getDescription());
    }
}