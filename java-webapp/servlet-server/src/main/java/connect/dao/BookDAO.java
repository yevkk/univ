package connect.dao;

import connect.dao.interfaces.BaseDAO;
import entity.book.Book;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class BookDAO implements BaseDAO<Book> {
    private final Connection conn;
    private final ResourceBundle resBundle = ResourceBundle.getBundle("database");
    private final Logger logger = Logger.getLogger(BookDAO.class.getName());

    public BookDAO(Connection conn) {
        this.conn = conn;
    }

    private Book mapper(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var name = rs.getString("name");
        var author = rs.getString("author");
        var lang = rs.getString("lang");
        var tags = (String[]) rs.getArray("tags").getArray();
        return new Book(id, name, author, lang, tags);
    }

    @Override
    public List<Book> findAll() {
        try (var statement = conn.createStatement()) {
            try (var resultSet = statement.executeQuery(resBundle.getString("query.book.findAll"))) {
                var list = new ArrayList<Book>();
                while (resultSet.next()) {
                    list.add(mapper(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            logger.warning("SQLException in findAll()");
        }
        return null;
    }

    @Override
    public Book find(int id) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.book.find"))) {
            statement.setInt(1, id);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapper(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.warning("SQLException in find()");
        }
        return null;
    }

    @Override
    public boolean create(Book entity) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.book.create"))) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getAuthor());
            statement.setString(3, entity.getLang());
            statement.setArray(4, conn.createArrayOf("text", entity.getTags()));

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in create()");
        }
        return res;
    }

    @Override
    public boolean update(Book entity) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.book.update"))) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getAuthor());
            statement.setString(3, entity.getLang());
            statement.setArray(4, conn.createArrayOf("text", entity.getTags()));
            statement.setInt(5, entity.getId());

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in update()");
        }
        return res;
    }

    @Override
    public boolean delete(int id) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.book.delete"))) {
            statement.setInt(1, id);

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in delete()");
        }
        return res;
    }
}
