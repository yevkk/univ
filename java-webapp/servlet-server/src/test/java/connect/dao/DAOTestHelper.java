package connect.dao;

import connect.ConnectionPool;
import connect.dao.interfaces.BaseDAO;
import connect.dao.interfaces.InsertFindDAO;
import entity.Entity;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DAOTestHelper {
    public static void clearAfter(ConnectionPool pool) throws SQLException {
        var conn = pool.getConnection();
        var callableStatement = conn.prepareCall("CALL truncate_all()");
        callableStatement.execute();
    }

    public static <E extends Entity> void insertAndFindTest (InsertFindDAO<E> dao, E e1, E e2, E e3) {
        assertEquals(0, dao.findAll().size());

        dao.create(e1);
        assertEquals(e1, dao.find(1));
        assertEquals(1, dao.findAll().size());
        assertTrue(dao.findAll().contains(e1));

        dao.create(e2);
        assertEquals(e2, dao.find(2));
        assertEquals(2, dao.findAll().size());
        assertTrue(dao.findAll().contains(e1));
        assertTrue(dao.findAll().contains(e2));

        dao.create(e3);
        assertEquals(e3, dao.find(3));
        assertEquals(3, dao.findAll().size());
        assertTrue(dao.findAll().contains(e1));
        assertTrue(dao.findAll().contains(e2));
        assertTrue(dao.findAll().contains(e3));
    }

    public static <E extends Entity> void deleteTest (BaseDAO<E> dao) {
        var book1 = dao.find(1);
        var book2 = dao.find(2);
        var book3 = dao.find(3);

        assertEquals(3, dao.findAll().size());
        assertTrue(dao.findAll().contains(book1));
        assertTrue(dao.findAll().contains(book2));
        assertTrue(dao.findAll().contains(book3));

        dao.delete(2);
        assertEquals(2, dao.findAll().size());
        assertTrue(dao.findAll().contains(book1));
        assertTrue(dao.findAll().contains(book3));

        dao.delete(1);
        assertEquals(1, dao.findAll().size());
        assertTrue(dao.findAll().contains(book3));

        //repeated delete, shows no changes
        dao.delete(2);
        assertEquals(1, dao.findAll().size());
        assertTrue(dao.findAll().contains(book3));

        dao.delete(3);
        assertEquals(0, dao.findAll().size());
    }
}
