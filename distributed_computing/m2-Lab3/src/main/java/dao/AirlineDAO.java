package dao;

import entites.Airline;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AirlineDAO implements BaseDAO<Airline> {
    private final ResourceBundle queryBundle = ResourceBundle.getBundle("query");
    private final Logger logger = Logger.getLogger(AirlineDAO.class.getName());

    @Override
    public List<Airline> findAll() {
        try (var connection = ConnectorDB.getConnection()){
            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery(queryBundle.getString("airline.findAll"))) {
                    var list = new ArrayList<Airline>();
                    while (resultSet.next()) {
                        var id = resultSet.getInt(1);
                        var name = resultSet.getString(2);
                        var country = resultSet.getString(3);
                        list.add(new Airline(id, name, country));
                    }

                    return list;
                }
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return null;
    }

    @Override
    public Airline find(int id) {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(queryBundle.getString("airline.find"))) {
                statement.setInt(1, id);

                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        var name = resultSet.getString(2);
                        var country = resultSet.getString(3);

                        return new Airline(id, name, country);
                    }
                }
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean create(Airline entity) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(queryBundle.getString("airline.insert"))) {
                statement.setString(1, entity.getName());
                statement.setString(2, entity.getCountry());
                statement.executeUpdate();
                res = true;
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return res;
    }

    @Override
    public boolean update(Airline entity) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(queryBundle.getString("airline.update"))) {
                statement.setString(1, entity.getName());
                statement.setString(2, entity.getCountry());
                statement.setInt(3, entity.getId());
                statement.executeUpdate();
                res = true;
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return res;
    }

    @Override
    public boolean delete(int id) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(queryBundle.getString("airline.delete"))) {
                statement.setInt(1, id);
                statement.executeUpdate();
                res = true;
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return res;
    }
}
