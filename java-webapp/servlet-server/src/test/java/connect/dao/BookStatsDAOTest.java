package connect.dao;

import connect.ConnectionPool;
import entity.book.Book;
import entity.book.BookStats;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookStatsDAOTest {
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
        bookDao.create(new Book("book3", "author3", "pl", new String[]{}));
    }

    @AfterAll
    public static void clearAfter() throws SQLException {
        DAOTestHelper.clearAfter(pool);
        pool = null;
    }

    @Test
    @Order(1)
    public void insertAndFind() {
        var bookStats1 = new BookStats(1, 1, 1, 1, 1);
        var bookStats2 = new BookStats(2, 2, 2, 2, 2);
        var bookStats3 = new BookStats(3, 3, 3, 3, 3);

        DAOTestHelper.insertAndFindTest(new BookStatsDAO(conn), bookStats1, bookStats2, bookStats3);
    }

    @Test
    @Order(2)
    public void update() {
        var dao = new BookStatsDAO(conn);

        var bookStats1 = dao.find(1);
        bookStats1.setBookID(2);
        dao.update(bookStats1);
        assertEquals(2, dao.find(1).getBookID());

        var bookStats2 = dao.find(2);
        bookStats2.setAmount(10);
        bookStats2.setRate(5);
        dao.update(bookStats2);
        assertEquals(10, dao.find(2).getAmount());
        assertEquals(5, dao.find(2).getRate());

        var bookStats3 = dao.find(3);
        bookStats3.setId(1);
        dao.update(bookStats3);
        assertEquals(bookStats3, dao.find(1));
    }

    @Test
    @Order(3)
    public void delete() {
        DAOTestHelper.deleteTest(new BookStatsDAO(conn));
    }
}