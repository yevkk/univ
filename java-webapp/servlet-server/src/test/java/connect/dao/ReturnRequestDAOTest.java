package connect.dao;

import connect.ConnectionPool;
import entity.book.Book;

import entity.book.request.GetBookRequest;
import entity.book.request.RequestState;
import entity.book.request.ReturnBookRequest;
import entity.misc.DeliveryType;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReturnRequestDAOTest {
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

        var dao = new BookRequestDAO(conn);
        dao.create(new GetBookRequest(LocalDateTime.parse("2020-04-05T06:40:30"), 1, 1, 1, "", RequestState.SENT));
        dao.create(new GetBookRequest(LocalDateTime.parse("2010-12-13T15:02:00"), 1, 2, 1, "@test", RequestState.SENT));
        dao.create(new GetBookRequest(LocalDateTime.parse("2021-07-08T14:00:00"), 2, 2, 2, "", RequestState.SENT));

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
        var dao = new ReturnRequestDAO(conn);

        var rReq1 = new ReturnBookRequest(1, LocalDateTime.parse("2020-04-16T06:40:30"), 1, RequestState.SENT);
        var rReq2 = new ReturnBookRequest(2, LocalDateTime.parse("2011-01-13T15:02:00"), 2, RequestState.SENT);
        var rReq3 = new ReturnBookRequest(3, LocalDateTime.parse("2021-09-09T14:00:00"), 3, RequestState.SENT);

        DAOTestHelper.insertAndFindTest(dao, rReq1, rReq2, rReq3);
    }

    @Test
    @Order(2)
    public void findByUserID() {
        var dao = new ReturnRequestDAO(conn);

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
        var dao = new ReturnRequestDAO(conn);

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
        var dao = new ReturnRequestDAO(conn);

        var rReq1 = dao.find(1);
        rReq1.setState(RequestState.PROCESSED);
        dao.updateState(rReq1);
        assertEquals(RequestState.PROCESSED, dao.find(1).getState());

        var rReq2 = dao.find(2);
        rReq2.setState(RequestState.PROCESSED);
        dao.updateState(rReq2);
        assertEquals(RequestState.PROCESSED, dao.find(2).getState());
    }

}