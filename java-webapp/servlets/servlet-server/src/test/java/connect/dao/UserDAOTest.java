package connect.dao;

import connect.ConnectionPool;
import entity.misc.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Considering that test db has test data in users table
 * <ul>
 * <li><b>id:</b> 1, <b>login:</b> user1,     <b>password:</b> pass, <b>role:</b> user,   <b>contact:</b> @test</li>
 * <li><b>id:</b> 2, <b>login:</b> user2,     <b>password:</b> pass,  <b>role:</b> user,  <b>contact:</b> test@example.com</li>
 * <li><b>id:</b> 3, <b>login:</b> superuser, <b>password:</b> pass,  <b>role:</b> admin, <b>contact:</b> admin@example.com</li>
 * </ul>
 */
class UserDAOTest {
    private static ConnectionPool pool = new ConnectionPool("database-test");
    private Connection conn;
    private static List<User> users = new ArrayList<>();

    @BeforeEach
    public void setConnection() {
        conn = pool.getConnection();
    }

    @AfterEach
    public void returnConnection() {
        pool.putConnection(conn);
    }

    @BeforeAll
    public static void fillUsers() {
        users.add(new User(1, "user1", "pass", User.Role.USER, "@test"));
        users.add(new User(2, "user2", "pass", User.Role.USER, "test@example.com"));
        users.add(new User(3, "superuser", "pass", User.Role.ADMIN, "admin@example.com"));
    }

    @AfterAll
    public static void clearPool() {
        pool = null;
    }

    @Test
    public void findAll() {
        var dao = new UserDAO(conn);


        var list = dao.findAll();

        assertEquals(3, list.size());

        for (var user : users) {
            assertTrue(list.contains(user));
        }
    }

    @Test
    public void findSuccess() {
        var dao = new UserDAO(conn);

        for (var user : users) {
            assertEquals(user, dao.find(user.getLogin(), user.getPassword()));
        }
    }

    @Test
    public void findFailed() {
        var dao = new UserDAO(conn);

        assertNull(dao.find("user1", "pppas"));
        assertNull(dao.find("user12", "pass"));
        assertNull(dao.find("useruser", "passpass"));
    }

}