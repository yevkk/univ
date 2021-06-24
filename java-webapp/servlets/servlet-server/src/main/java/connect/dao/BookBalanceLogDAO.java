package connect.dao;

import connect.dao.interfaces.InsertFindDAO;
import entity.book.history.BookBalanceLogRecord;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class BookBalanceLogDAO implements InsertFindDAO<BookBalanceLogRecord> {
    private final Connection conn;
    private final ResourceBundle resBundle = ResourceBundle.getBundle("database");
    private final Logger logger = Logger.getLogger(BookBalanceLogDAO.class.getName());

    public BookBalanceLogDAO(Connection conn) {
        this.conn = conn;
    }

    private BookBalanceLogRecord mapper(ResultSet rs) throws SQLException{
        var id = rs.getInt("id");
        var datetime = rs.getTimestamp("datetime").toLocalDateTime();
        var bookID = rs.getInt("book_id");
        var amount = rs.getInt("amount");
        var comment = rs.getString("comment");

        return new BookBalanceLogRecord(id, datetime, bookID, amount, comment);
    }

    @Override
    public List<BookBalanceLogRecord> findAll() {
        try (var statement = conn.createStatement()) {
            try (var resultSet = statement.executeQuery(resBundle.getString("query.balance_changelog.findAll"))) {
                var list = new ArrayList<BookBalanceLogRecord>();
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
    public BookBalanceLogRecord find(int id) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.balance_changelog.find"))) {
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

    public List<BookBalanceLogRecord> findByBookID(int bookID) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.balance_changelog.findByBookID"))) {
            statement.setInt(1, bookID);

            try (var resultSet = statement.executeQuery()) {
                var list = new ArrayList<BookBalanceLogRecord>();
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

    public List<BookBalanceLogRecord> findInPeriod(LocalDateTime periodStart, LocalDateTime periodEnd) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.balance_changelog.findInPeriod"))) {
            statement.setTimestamp(1, Timestamp.valueOf(periodStart));
            statement.setTimestamp(2, Timestamp.valueOf(periodEnd));

            try (var resultSet = statement.executeQuery()) {
                var list = new ArrayList<BookBalanceLogRecord>();
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
    public boolean create(BookBalanceLogRecord entity) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.balance_changelog.create"))) {
            statement.setTimestamp(1, Timestamp.valueOf(entity.getDatetime()));
            statement.setInt(2, entity.getBookID());
            statement.setInt(3, entity.getAmount());
            statement.setString(4, entity.getComment());

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in create()");
        }
        return res;
    }
}
