package dao;

import entity.Region;
import entity.WeatherRecord;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeatherRecordDAO implements BaseDAO<WeatherRecord> {
    private static final String SQL_FIND_ALL = "SELECT * FROM \"weather-records\"";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM \"weather-records\" WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO \"weather-records\" (date, temperature, precipitation, region_id) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE_BY_ID = "UPDATE \"weather-records\" SET date = ?, temperature = ?, precipitation = ?, region_id = ? WHERE id = ?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM \"weather-records\" WHERE id = ?";

    @Override
    public List<WeatherRecord> findAll() {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery(SQL_FIND_ALL)) {

                    var list = new ArrayList<WeatherRecord>();
                    while (resultSet.next()) {
                        var id = resultSet.getInt(1);
                        var date = resultSet.getObject(2, LocalDate.class);
                        var temperature = resultSet.getInt(3);
                        var precipitation = resultSet.getString(4);
                        var regionID = resultSet.getInt(5);
                        list.add(new WeatherRecord(id, date, temperature, precipitation, regionID));
                    }

                    return list;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public WeatherRecord find(int id) {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
                statement.setInt(1, id);

                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        var date = resultSet.getObject(2, LocalDate.class);
                        var temperature = resultSet.getInt(3);
                        var precipitation = resultSet.getString(4);
                        var regionID = resultSet.getInt(5);
                        return new WeatherRecord(id, date, temperature, precipitation, regionID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(WeatherRecord entity) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_INSERT)) {
                statement.setDate(1, Date.valueOf(entity.getDate()));
                statement.setInt(2, entity.getTemperature());
                statement.setString(3, entity.getPrecipitation().toString());
                statement.setInt(4, entity.getRegionID());
                statement.executeUpdate();
                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public boolean update(WeatherRecord entity) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_UPDATE_BY_ID)) {
                statement.setDate(1, Date.valueOf(entity.getDate()));
                statement.setInt(2, entity.getTemperature());
                statement.setString(3, entity.getPrecipitation().toString());
                statement.setInt(4, entity.getRegionID());
                statement.setInt(5, entity.getId());
                statement.executeUpdate();
                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public boolean delete(int id) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
                statement.setInt(1, id);
                statement.executeUpdate();
                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
