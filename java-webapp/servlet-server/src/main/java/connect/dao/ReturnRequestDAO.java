package connect.dao;

import entity.book.request.GetBookRequest;
import entity.book.request.RequestState;
import entity.book.request.ReturnBookRequest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class ReturnRequestDAO implements InsertFindDAO<ReturnBookRequest> {
    private final Connection conn;
    private final ResourceBundle resBundle = ResourceBundle.getBundle("database");
    private final Logger logger = Logger.getLogger(ReturnRequestDAO.class.getName());

    public ReturnRequestDAO(Connection conn) {
        this.conn = conn;
    }

    private ReturnBookRequest mapper(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var datetime = rs.getTimestamp("datetime").toLocalDateTime();
        var request_id = rs.getInt("request_id");
        var state = RequestState.valueOf(rs.getString("state").toUpperCase());

        return new ReturnBookRequest(id, datetime, request_id, state);
    }

    @Override
    public List<ReturnBookRequest> findAll() {
        try (var statement = conn.createStatement()) {
            try (var resultSet = statement.executeQuery(resBundle.getString("query.return_request.findAll"))) {
                var list = new ArrayList<ReturnBookRequest>();
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
    public ReturnBookRequest find(int id) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.return_request.find"))) {
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

    public List<ReturnBookRequest> findByUserID(int userID) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.return_request.findByUserID"))) {
            statement.setInt(1, userID);

            try (var resultSet = statement.executeQuery()) {
                var list = new ArrayList<ReturnBookRequest>();
                while (resultSet.next()) {
                    list.add(mapper(resultSet));
                }
                return list;
            }
        } catch (SQLException e) {
            logger.warning("SQLException in findByUserID()");
        }
        return null;
    }

    public List<ReturnBookRequest> findByBookID(int bookID) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.return_request.findByBookID"))) {
            statement.setInt(1, bookID);

            try (var resultSet = statement.executeQuery()) {
                var list = new ArrayList<ReturnBookRequest>();
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

    @Override
    public boolean create(ReturnBookRequest entity) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.return_request.create"))) {
            statement.setTimestamp(1, Timestamp.valueOf(entity.getDatetime()));
            statement.setInt(2, entity.getGetBookRequestID());
            statement.setString(3, entity.getState().toString());

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in create()");
        }
        return res;
    }

    public boolean updateState(ReturnBookRequest entity) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.return_request.updateState"))) {
            statement.setString(1, entity.getState().toString());
            statement.setInt(2, entity.getId());

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in updateState()");
        }
        return res;
    }
}
