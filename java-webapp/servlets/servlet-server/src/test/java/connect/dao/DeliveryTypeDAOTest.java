package connect.dao;

import connect.ConnectionPool;
import entity.book.Book;
import entity.book.BookStats;
import entity.misc.DeliveryType;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeliveryTypeDAOTest {
    private static ConnectionPool pool = new ConnectionPool("database-test");
    private Connection conn;

    @BeforeEach
    public void setConnection() {
        conn = pool.getConnection();
    }

    @AfterEach
    public void returnConnection() {
        pool.putConnection(conn);
    }

    @AfterAll
    public static void clearAfter() throws SQLException {
        DAOTestHelper.clearAfter(pool);
        pool = null;
    }

    @Test
    @Order(1)
    public void insertAndFind() {
        var deliveryType1 = new DeliveryType(1, "home");
        var deliveryType2 = new DeliveryType(2, "univ");
        var deliveryType3 = new DeliveryType(3, "post");

        DAOTestHelper.insertAndFindTest(new DeliveryTypeDAO(conn), deliveryType1, deliveryType2, deliveryType3);
    }

    @Test
    @Order(2)
    public void update() {
        var dao = new DeliveryTypeDAO(conn);

        var deliveryType1 = dao.find(1);
        deliveryType1.setDescription("new description");
        dao.update(deliveryType1);
        assertEquals("new description", dao.find(1).getDescription());

        var deliveryType2 = dao.find(3);
        deliveryType2.setId(1);
        dao.update(deliveryType2);
        assertEquals(deliveryType2, dao.find(1));
    }

    @Test
    @Order(3)
    public void delete() {
        DAOTestHelper.deleteTest(new DeliveryTypeDAO(conn));
    }

}