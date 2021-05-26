package connect.dao;

import entity.book.Book;
import entity.misc.DeliveryType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class DeliveryTypeDAO implements BaseDAO<DeliveryType> {
    private final Connection conn;
    private final ResourceBundle resBundle = ResourceBundle.getBundle("database");
    private final Logger logger = Logger.getLogger(DeliveryTypeDAO.class.getName());

    public DeliveryTypeDAO(Connection conn) {
        this.conn = conn;
    }

    private DeliveryType mapper(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var description = rs.getString("description");
        return new DeliveryType(id, description);
    }

    @Override
    public List<DeliveryType> findAll() {
        try (var statement = conn.createStatement()) {
            try (var resultSet = statement.executeQuery(resBundle.getString("query.delivery_type.findAll"))) {
                var list = new ArrayList<DeliveryType>();
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
    public DeliveryType find(int id) {
        try (var statement = conn.prepareStatement(resBundle.getString("query.delivery_type.find"))) {
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
    public boolean create(DeliveryType entity) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.delivery_type.create"))) {
            statement.setString(1, entity.getDescription());

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in create()");
        }
        return res;
    }

    @Override
    public boolean update(DeliveryType entity) {
        var res = false;
        try (var statement = conn.prepareStatement(resBundle.getString("query.delivery_type.update"))) {
            statement.setString(1, entity.getDescription());
            statement.setInt(2, entity.getId());

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
        try (var statement = conn.prepareStatement(resBundle.getString("query.delivery_type.delete"))) {
            statement.setInt(1, id);

            statement.executeUpdate();
            res = true;
        } catch (SQLException e) {
            logger.warning("SQLException in delete()");
        }
        return res;
    }
}
