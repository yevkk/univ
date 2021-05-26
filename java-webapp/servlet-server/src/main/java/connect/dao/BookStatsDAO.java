package connect.dao;

import entity.book.BookStats;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class BookStatsDAO implements BaseDAO<BookStats> {
    private final Connection conn;
    private final ResourceBundle resBundle = ResourceBundle.getBundle("database");
    private final Logger logger = Logger.getLogger(BookStatsDAO.class.getName());

    public BookStatsDAO(Connection conn) {
        this.conn = conn;
    }

    private BookStats mapper(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var bookID = rs.getInt("book_id");
        var amount = rs.getInt("amount");
        var totalRequests = rs.getInt("total_requests");
        var rate = rs.getInt("rate");
        return new BookStats(id, bookID, amount, totalRequests, rate);
    }

    @Override
    public List<BookStats> findAll() {
        try (var statement = conn.createStatement()) {
            try (var resultSet = statement.executeQuery(resBundle.getString("query.book_stats.findAll"))) {
                var list = new ArrayList<BookStats>();
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
    public BookStats find(int id) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.book_stats.find"))) {
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
    public boolean create(BookStats entity) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.book_stats.create"))) {
            statement.setInt(1, entity.getBookID());
            statement.setInt(2, entity.getAmount());
            statement.setInt(3, entity.getTotalRequests());
            statement.setDouble(4, entity.getRate());

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in create()");
        }
        return res;
    }

    @Override
    public boolean update(BookStats entity) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.book_stats.update"))) {
            statement.setInt(1, entity.getBookID());
            statement.setInt(2, entity.getAmount());
            statement.setInt(3, entity.getTotalRequests());
            statement.setDouble(4, entity.getRate());
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
        try (var statement = conn.prepareStatement(resBundle.getString("query.book_stats.delete"))) {
            statement.setInt(1, id);

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in delete()");
        }
        return res;
    }
}
