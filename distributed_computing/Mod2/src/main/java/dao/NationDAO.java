package dao;

import entity.Nation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NationDAO implements BaseDAO<Nation> {
    private static final String SQL_FIND_ALL = "SELECT * FROM nations";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM nations WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO nations (name, language) VALUES(?, ?)";
    private static final String SQL_UPDATE_BY_ID = "UPDATE nations SET name = ?, language = ? WHERE id = ?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM nations WHERE id = ?";

    @Override
    public List<Nation> findAll() {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery(SQL_FIND_ALL)) {

                    var list = new ArrayList<Nation>();
                    while (resultSet.next()) {
                        var id = resultSet.getInt(1);
                        var name = resultSet.getString(2);
                        var language = resultSet.getString(3);
                        list.add(new Nation(id, name, language));
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
    public Nation find(int id) {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
                statement.setInt(1, id);

                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        var name = resultSet.getString(2);
                        var language = resultSet.getString(3);
                        return new Nation(id, name, language);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Nation entity) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_INSERT)) {
                statement.setString(1, entity.getName());
                statement.setString(2, entity.getLanguage());
                statement.executeUpdate();
                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public boolean update(Nation entity) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_UPDATE_BY_ID)) {
                statement.setString(1, entity.getName());
                statement.setString(2, entity.getLanguage());
                statement.setInt(3, entity.getId());
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
