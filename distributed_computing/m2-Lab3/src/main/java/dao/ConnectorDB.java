package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class ConnectorDB {
    public static Connection getConnection() throws SQLException {
        var resource = ResourceBundle.getBundle("database");
        var url = resource.getString("db.url");

        var props = new Properties();
        props.put("user", resource.getString("db.user"));
        props.put("password", resource.getString("db.password"));
        props.put("characterEncoding", resource.getString("db.characterEncoding"));

        return DriverManager.getConnection(url, props);
    }
}
