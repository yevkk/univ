package dao;

import entities.Airline;
import entities.Flight;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class FlightDAO implements BaseDAO<Flight> {
    private final ResourceBundle queryBundle = ResourceBundle.getBundle("query");
    private final Logger logger = Logger.getLogger(FlightDAO.class.getName());

    @Override
    public List<Flight> findAll() {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery(queryBundle.getString("flight.findAll"))) {
                    var list = new ArrayList<Flight>();
                    while (resultSet.next()) {
                        var id = resultSet.getInt(1);
                        var airlineId = resultSet.getInt(2);
                        var from = resultSet.getString(3);
                        var to = resultSet.getString(4);
                        var price = resultSet.getDouble(5);
                        list.add(new Flight(id, airlineId, from, to, price));
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
    public Flight find(int id) {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(queryBundle.getString("flight.find"))) {
                statement.setInt(1, id);
                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        var airlineId = resultSet.getInt(2);
                        var from = resultSet.getString(3);
                        var to = resultSet.getString(4);
                        var price = resultSet.getDouble(5);
                        return new Flight(id, airlineId, from, to, price);
                    }
                }
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean create(Flight entity) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(queryBundle.getString("flight.insert"))) {
                statement.setInt(1, entity.getAirlineId());
                statement.setString(2, entity.getFrom());
                statement.setString(3, entity.getTo());
                statement.setDouble(4, entity.getPrice());
                statement.executeUpdate();
                res = true;
            }
        } catch (SQLException e) {
            logger.warning(e.getMessage());
        }
        return res;
    }

    @Override
    public boolean update(Flight entity) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(queryBundle.getString("flight.update"))) {
                statement.setInt(1, entity.getAirlineId());
                statement.setString(2, entity.getFrom());
                statement.setString(3, entity.getTo());
                statement.setDouble(4, entity.getPrice());
                statement.setInt(5, entity.getId());
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
            try (var statement = connection.prepareStatement(queryBundle.getString("flight.delete"))) {
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
