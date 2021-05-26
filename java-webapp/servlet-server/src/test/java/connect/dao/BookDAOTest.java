package connect.dao;

import connect.ConnectionPool;
import entity.book.Book;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookDAOTest {
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
        var book1 = new Book(1, "name1", "author1", "lang1", new String[]{"Tag1", "Tag2"});
        var book2 = new Book(2, "name2", "author2", "lang2", new String[]{"Tag3"});
        var book3 = new Book(3, "name3", "author3", "lang3", new String[]{"Tag1"});

        DAOTestHelper.insertAndFindTest(new BookDAO(conn), book1, book2, book3);
    }

    @Test
    @Order(2)
    public void update() {
        var dao = new BookDAO(conn);

        var book1 = dao.find(1);
        book1.setName("definitely new name");
        dao.update(book1);
        assertEquals("definitely new name", dao.find(1).getName());

        var book2 = dao.find(2);
        book2.setAuthor("definitely new author");
        book2.setLang("definitely new lang");
        dao.update(book2);
        assertEquals("definitely new author", dao.find(2).getAuthor());
        assertEquals("definitely new lang", dao.find(2).getLang());

        var book3 = dao.find(3);
        book3.setId(1);
        dao.update(book3);
        assertEquals(book3, dao.find(1));
    }

    @Test
    @Order(3)
    public void delete() {
        DAOTestHelper.deleteTest(new BookDAO(conn));
    }
}