package connect.dao;

import connect.ConnectionPool;
import entity.book.Book;
import entity.book.request.GetBookRequest;
import entity.book.request.RequestState;
import entity.misc.DeliveryType;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Considering that test db has test data in users table<br/>
 * See {@link UserDAOTest} description for more info
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookRequestDAOTest {
    private static ConnectionPool pool= new ConnectionPool("database-test");
    private Connection conn;

    @BeforeEach
    public void setConnection() {
        conn = pool.getConnection();
    }

    @AfterEach
    public void returnConnection() {
        pool.putConnection(conn);
    }

    @BeforeAll
    public static void fillDatabase() {
        var conn = pool.getConnection();
        var bookDao = new BookDAO(conn);
        bookDao.create(new Book("book1", "author1", "ua", new String[]{}));
        bookDao.create(new Book("book2", "author2", "en", new String[]{}));

        var deliveryTypeDao = new DeliveryTypeDAO(conn);
        deliveryTypeDao.create(new DeliveryType("home"));
        deliveryTypeDao.create(new DeliveryType("univ"));

        pool.putConnection(conn);
    }

    @AfterAll
    public static void clearAfter() throws SQLException {
        DAOTestHelper.clearAfter(pool);
        pool = null;
    }

    @Test
    @Order(1)
    public void insertAndFindAll() {
        var dao = new BookRequestDAO(conn);

        var req1 = new GetBookRequest(1, LocalDateTime.parse("2020-04-05T06:40:30"), 1, 1, 1, "@test1", RequestState.SENT);
        var req2 = new GetBookRequest(2, LocalDateTime.parse("2010-12-13T15:02:00"), 1, 2, 1, "@test2", RequestState.SENT);
        var req3 = new GetBookRequest(3, LocalDateTime.parse("2021-07-08T14:00:00"), 2, 2, 2, "@test3", RequestState.SENT);

        DAOTestHelper.insertAndFindTest(dao, req1, req2, req3);
    }

    @Test
    @Order(2)
    public void findByUserID() {
        var dao = new BookRequestDAO(conn);

        var list1 = dao.findByUserID(1);
        assertTrue(list1.contains(dao.find(1)));
        assertTrue(list1.contains(dao.find(2)));
        assertFalse(list1.contains(dao.find(3)));

        var list2 = dao.findByUserID(2);
        assertFalse(list2.contains(dao.find(1)));
        assertFalse(list2.contains(dao.find(2)));
        assertTrue(list2.contains(dao.find(3)));
    }

    @Test
    @Order(3)
    public void findByBookID() {
        var dao = new BookRequestDAO(conn);

        var list1 = dao.findByBookID(1);
        assertTrue(list1.contains(dao.find(1)));
        assertFalse(list1.contains(dao.find(2)));
        assertFalse(list1.contains(dao.find(3)));

        var list2 = dao.findByBookID(2);
        assertFalse(list2.contains(dao.find(1)));
        assertTrue(list2.contains(dao.find(2)));
        assertTrue(list2.contains(dao.find(3)));
    }

    @Test
    @Order(4)
    public void updateState() {
        var dao = new BookRequestDAO(conn);

        var req1 = dao.find(1);
        req1.setState(RequestState.IN_PROGRESS);
        dao.updateState(req1);
        assertEquals(RequestState.IN_PROGRESS, dao.find(1).getState());

        var req2 = dao.find(2);
        req2.setState(RequestState.REJECTED);
        dao.updateState(req2);
        assertEquals(RequestState.REJECTED, dao.find(2).getState());
    }

}