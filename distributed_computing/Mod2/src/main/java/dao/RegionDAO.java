package dao;

import entity.Region;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionDAO implements BaseDAO<Region> {
    private static final String SQL_FIND_ALL = "SELECT * FROM regions";
    private static final String SQL_FIND_BY_ID = "SELECT * FROM regions WHERE id = ?";
    private static final String SQL_INSERT = "INSERT INTO regions (name, square, nation_id) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_BY_ID = "UPDATE regions SET name = ?, square = ?, nation_id = ? WHERE id = ?";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM regions WHERE oid = ?";

    @Override
    public List<Region> findAll() {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.createStatement()) {
                try (var resultSet = statement.executeQuery(SQL_FIND_ALL)) {

                    var list = new ArrayList<Region>();
                    while (resultSet.next()) {
                        var id = resultSet.getInt(1);
                        var name = resultSet.getString(2);
                        var square = resultSet.getDouble(3);
                        var nationID = resultSet.getInt(4);
                        list.add(new Region(id, name, square, nationID));
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
    public Region find(int id) {
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
                statement.setInt(1, id);

                try (var resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        var name = resultSet.getString(2);
                        var square = resultSet.getDouble(3);
                        var nationID = resultSet.getInt(4);
                        return new Region(id, name, square, nationID);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Region entity) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_INSERT)) {
                statement.setString(1, entity.getName());
                statement.setDouble(2, entity.getSquare());
                statement.setInt(3, entity.getNationID());
                statement.executeUpdate();
                res = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public boolean update(Region entity) {
        var res = false;
        try (var connection = ConnectorDB.getConnection()) {
            try (var statement = connection.prepareStatement(SQL_UPDATE_BY_ID)) {
                statement.setString(1, entity.getName());
                statement.setDouble(2, entity.getSquare());
                statement.setInt(3, entity.getNationID());
                statement.setInt(4, entity.getId());
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
