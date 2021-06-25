package entity;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    @Test
    public void emptyConstructor() {
        var e = new Entity();
        assertEquals(-1, e.getId());
    }

    @Test
    public void constructor() {
        var id1 = 4;
        var id2 = 10;
        var id3 = 37;

        var e1 = new Entity(id1);
        var e2 = new Entity(id2);
        var e3 = new Entity(id3);

        assertEquals(id1, e1.getId());
        assertEquals(id2, e2.getId());
        assertEquals(id3, e3.getId());
    }
}