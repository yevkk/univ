package connect.dao;

import connect.dao.interfaces.InsertFindDAO;
import entity.book.BookStats;
import entity.book.history.BookStatsHistoryRecord;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class BookStatsHistoryDAO implements InsertFindDAO<BookStatsHistoryRecord> {
    private final Connection conn;
    private final ResourceBundle resBundle = ResourceBundle.getBundle("database");
    private final Logger logger = Logger.getLogger(BookStatsHistoryDAO.class.getName());

    public BookStatsHistoryDAO(Connection conn) {
        this.conn = conn;
    }

    private BookStatsHistoryRecord mapper(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var date = rs.getDate("date").toLocalDate();
        var bookID = rs.getInt("book_id");
        var amount = rs.getInt("amount");
        var totalRequests = rs.getInt("total_requests");
        var rate = rs.getDouble("rate");

        return new BookStatsHistoryRecord(id, date, new BookStats(bookID, amount, totalRequests, rate));
    }

    @Override
    public List<BookStatsHistoryRecord> findAll() {
        try (var statement = conn.createStatement()) {
            try (var resultSet = statement.executeQuery(resBundle.getString("query.stats_history.findAll"))) {
                var list = new ArrayList<BookStatsHistoryRecord>();
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
    public BookStatsHistoryRecord find(int id) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.stats_history.find"))) {
            statement.setInt(1, id);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapper(resultSet);
                }
                return null;
            }
        } catch (SQLException e) {
            logger.warning("SQLException in find()");
        }
        return null;
    }

    public List<BookStatsHistoryRecord> findByBookID(int bookID) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.stats_history.findByBookID"))) {
            statement.setInt(1, bookID);

            try (var resultSet = statement.executeQuery()) {
                var list = new ArrayList<BookStatsHistoryRecord>();
                while (resultSet.next()) {
                    list.add(mapper(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            logger.warning("SQLException in findByBookID()");
        }
        return null;
    }

    public List<BookStatsHistoryRecord> findInPeriod(LocalDate periodStart, LocalDate periodEnd) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.stats_history.findInPeriod"))) {
            statement.setDate(1, Date.valueOf(periodStart));
            statement.setDate(2, Date.valueOf(periodEnd));

            try (var resultSet = statement.executeQuery()) {
                var list = new ArrayList<BookStatsHistoryRecord>();
                while (resultSet.next()) {
                    list.add(mapper(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            logger.warning("SQLException in findInPeriod()");
        }
        return null;
    }

    @Override
    public boolean create(BookStatsHistoryRecord entity) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.stats_history.create"))) {
            statement.setDate(1, Date.valueOf(entity.getDate()));
            statement.setInt(2, entity.getStats().getBookID());
            statement.setInt(3, entity.getStats().getAmount());
            statement.setInt(4, entity.getStats().getTotalRequests());
            statement.setDouble(5, entity.getStats().getRate());

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in create()");
        }
        return res;
    }
}
