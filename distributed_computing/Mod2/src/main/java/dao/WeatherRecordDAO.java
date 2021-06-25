package dao;

import entity.Region;
import entity.WeatherRecord;

import java.sql.Date;
import java.sql.ResultSet;
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
    private static final String SQL_FIND_BY_REGION_ID = "SELECT * FROM \"weather-records\" WHERE region_id = ? ORDER BY date";
    private static final String SQL_FIND_BY_NATION_LANGUAGE_LAST_WEEK = "SELECT * FROM \"weather-records\" WHERE date > (current_date - 7) AND region_id IN (SELECT id FROM regions WHERE nation_ID IN (SELECT id FROM nations WHERE language = ?))";
    private static final String SQL_FIND_DATES_BY_CONDITIONS = "SELECT date FROM \"weather-records\" WHERE region_id = ? AND precipitation = 'show' AND temperature < ?";
    private static final String SQL_FIND_AV_TEMPERATURE_BY_CONDITIONS = "SELECT AVG(temperature)::numeric(10,0) FROM \"weather-records\" WHERE date > (current_date - 7) AND region_id IN (SELECT id FROM regions WHERE square > ?)";

    private static WeatherRecord takeWeatherRecord(ResultSet resultSet) throws SQLException {
        var id = resultSet.getInt(1);
        var date = resultSet.getObject(2, LocalDate.class);
        var temperature = resultSet.getInt(3);
        var precipitation = resultSet.getString(4);
        var regionID = resultSet.getInt(5);

        return new WeatherRecord(id, date, temperature, precipitation, regionID);
    }

    @Override
    public List<WeatherRecord> findAll() {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery(SQL_FIND_ALL)) {

                    var list = new ArrayList<WeatherRecord>();
                    while (resultSet.next()) {
                        list.add(takeWeatherRecord(resultSet));
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
                        return takeWeatherRecord(resultSet);
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

    public List<WeatherRecord> findByRegion(Region region) {
        return findByRegion(region.getId());
    }

    public List<WeatherRecord> findByRegion(int regionID) {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_FIND_BY_REGION_ID)) {
                statement.setInt(1, regionID);

                try (var resultSet = statement.executeQuery()) {
                    var list = new ArrayList<WeatherRecord>();
                    while (resultSet.next()) {
                        list.add(takeWeatherRecord(resultSet));
                    }

                    return list;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<WeatherRecord> findByNationLanguageForLastWeek(String language) {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_FIND_BY_NATION_LANGUAGE_LAST_WEEK)) {
                statement.setString(1, language);

                try (var resultSet = statement.executeQuery()) {
                    var list = new ArrayList<WeatherRecord>();
                    while (resultSet.next()) {
                        list.add(takeWeatherRecord(resultSet));
                    }

                    return list;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Invokes {@link #findDatesByRegionWithConditions(int regionID, int temperature)}
     */
    public List<LocalDate> findDatesByRegionWithConditions(Region region, int temperature) {
        return findDatesByRegionWithConditions(region.getId(), temperature);
    }

    /**
     * Conditions: snow and temperature lower than given
     */
    public List<LocalDate> findDatesByRegionWithConditions(int regionID, int temperature) {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_FIND_DATES_BY_CONDITIONS)) {
                statement.setInt(1, regionID);
                statement.setInt(2, temperature);

                try (var resultSet = statement.executeQuery()) {
                    var list = new ArrayList<LocalDate>();
                    while (resultSet.next()) {
                        list.add(resultSet.getObject(1, LocalDate.class));
                    }

                    return list;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Conditions: square greater than given
     */
    public double findAverageTemperatureInRegionsWithConditionsForLastWeek(double square) {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_FIND_AV_TEMPERATURE_BY_CONDITIONS)) {
                statement.setDouble(1, square);

                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
