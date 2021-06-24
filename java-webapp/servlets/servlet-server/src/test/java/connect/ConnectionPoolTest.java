package connect;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionPoolTest {
    private ConnectionPool pool;

    @BeforeEach
    public void before() {
        pool = new ConnectionPool("database-test");
    }

    @Test
    public void exceedConnectionLimit() {
        for (int i = 0; i < pool.getLimit(); i++) {
            assertNotNull(pool.getConnection());
        }

        assertNull(pool.getConnection());
        assertNull(pool.getConnection());
    }

    @Test
    public void exceedConnectionLimitPutBack() {
        for (int i = 0; i < pool.getLimit() - 2; i++) {
            assertNotNull(pool.getConnection());
        }
        var conn1 = pool.getConnection();
        var conn2 = pool.getConnection();

        assertNotNull(conn1);
        assertNotNull(conn2);

        assertNull(pool.getConnection());
        assertNull(pool.getConnection());

        pool.putConnection(conn1);
        pool.putConnection(conn2);

        assertNotNull(pool.getConnection());
        assertNotNull(pool.getConnection());

        assertNull(pool.getConnection());
        assertNull(pool.getConnection());
    }
}