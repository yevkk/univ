package connect.dao;

import connect.ConnectionPool;
import entity.book.Book;
import entity.book.history.BookBalanceLogRecord;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookBalanceLogDAOTest {
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

    @BeforeAll
    public static void fillDatabase() {
        var conn = pool.getConnection();
        var bookDao = new BookDAO(conn);
        bookDao.create(new Book("book1", "author1", "ua", new String[]{}));
        bookDao.create(new Book("book2", "author2", "en", new String[]{}));
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
        var record1 = new BookBalanceLogRecord(1, LocalDateTime.parse("2020-09-09T05:05:05"), 1, 2, "gift from _");
        var record2 = new BookBalanceLogRecord(2, LocalDateTime.parse("2021-03-02T07:07:07"), 2, 1, "");
        var record3 = new BookBalanceLogRecord(3, LocalDateTime.parse("2021-04-05T02:02:02"), 1, -1, "");

        DAOTestHelper.insertAndFindTest(new BookBalanceLogDAO(conn), record1, record2, record3);
    }

    @Test
    @Order(2)
    public void findByBookID() {
        var dao = new BookBalanceLogDAO(conn);

        var list1 = dao.findByBookID(1);
        assertEquals(2, list1.size());
        assertTrue(list1.contains(dao.find(1)));
        assertTrue(list1.contains(dao.find(3)));

        var list2 = dao.findByBookID(2);
        assertEquals(1, list2.size());
        assertTrue(list2.contains(dao.find(2)));
    }

    @Test
    @Order(3)
    public void findInPeriod() {
        var dao = new BookBalanceLogDAO(conn);

        var list1 = dao.findInPeriod(LocalDateTime.parse("2021-12-12T00:00:00"), LocalDateTime.parse("2021-12-13T00:00:00"));
        assertTrue(list1.isEmpty());

        var list2 = dao.findInPeriod(LocalDateTime.parse("2021-02-17T00:00:00"), LocalDateTime.parse("2021-06-07T00:00:00"));
        assertEquals(2, list2.size());
        assertTrue(list2.contains(dao.find(2)));
        assertTrue(list2.contains(dao.find(3)));

        var list3 = dao.findInPeriod(LocalDateTime.parse("2020-01-01T00:00:00"), LocalDateTime.parse("2020-12-31T00:00:00"));
        assertEquals(1, list3.size());
        assertTrue(list3.contains(dao.find(1)));
    }

}