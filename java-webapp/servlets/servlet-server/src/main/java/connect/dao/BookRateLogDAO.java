package connect.dao;

import connect.dao.interfaces.InsertFindDAO;
import entity.book.history.BookBalanceLogRecord;
import entity.book.history.BookRateLogRecord;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class BookRateLogDAO implements InsertFindDAO<BookRateLogRecord> {
    private final Connection conn;
    private final ResourceBundle resBundle = ResourceBundle.getBundle("database");
    private final Logger logger = Logger.getLogger(BookRateLogDAO.class.getName());

    public BookRateLogDAO(Connection conn) {
        this.conn = conn;
    }

    private BookRateLogRecord mapper(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var datetime = rs.getTimestamp("datetime").toLocalDateTime();
        var bookID = rs.getInt("book_id");
        var userID = rs.getInt("user_id");
        var contribution = rs.getDouble("contribution");

        return new BookRateLogRecord(id, datetime, bookID, userID, contribution);
    }

    @Override
    public List<BookRateLogRecord> findAll() {
        try (var statement = conn.createStatement()) {
            try (var resultSet = statement.executeQuery(resBundle.getString("query.rate_changelog.findAll"))) {
                var list = new ArrayList<BookRateLogRecord>();
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
    public BookRateLogRecord find(int id) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.rate_changelog.find"))) {
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

    public List<BookRateLogRecord> findByBookID(int bookID) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.rate_changelog.findByBookID"))) {
            statement.setInt(1, bookID);

            try (var resultSet = statement.executeQuery()) {
                var list = new ArrayList<BookRateLogRecord>();
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

    public List<BookRateLogRecord> findInPeriod(LocalDateTime periodStart, LocalDateTime periodEnd) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.rate_changelog.findInPeriod"))) {
            statement.setTimestamp(1, Timestamp.valueOf(periodStart));
            statement.setTimestamp(2, Timestamp.valueOf(periodEnd));

            try (var resultSet = statement.executeQuery()) {
                var list = new ArrayList<BookRateLogRecord>();
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
    public boolean create(BookRateLogRecord entity) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.rate_changelog.create"))) {
            statement.setTimestamp(1, Timestamp.valueOf(entity.getDatetime()));
            statement.setInt(2, entity.getBookID());
            statement.setInt(3, entity.getUserID());
            statement.setDouble(4, entity.getContribution());

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in create()");
        }
        return res;
    }
}
