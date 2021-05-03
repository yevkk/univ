package dao;

import java.sql.*;
import java.util.ResourceBundle;
import java.util.Properties;

public class ConnectorDB {
    public static Connection getConnection() throws SQLException {
        ResourceBundle resource = ResourceBundle.getBundle("database");
        String url = resource.getString("db.url");

        Properties props = new Properties();
        props.put("user", resource.getString("db.user"));
        props.put("password", resource.getString("db.password"));
        props.put("characterEncoding", resource.getString("db.characterEncoding"));

        return DriverManager.getConnection(url, props);
    }
}
