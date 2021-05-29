package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class ConnectionPool {
    private final int limit;
    private final List<Connection> connections;
    private final Logger logger = Logger.getLogger(ConnectionPool.class.getName());

    public ConnectionPool(String setupResourceBundleName) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.warning("Driver not found");
        }

        var resource = ResourceBundle.getBundle(setupResourceBundleName);
        limit = Integer.parseInt(resource.getString("setup.maxConnections"));
        connections = new ArrayList<>();

        var url = resource.getString("setup.url");
        var props = new Properties();
        props.put("user", resource.getString("setup.user"));
        props.put("password", resource.getString("setup.password"));
        props.put("characterEncoding", resource.getString("setup.characterEncoding"));

        try {
            for (int i = 0; i < limit; i++) {
                connections.add(DriverManager.getConnection(url, props));
            }
        } catch (SQLException e) {
            logger.warning("Cannot create enough connections");
        }
    }

    public Connection getConnection() {
        Connection conn;
        synchronized (this) {
            if (connections.isEmpty()) {
                logger.warning("Connections limit exceeded");
                return null;
            }
            conn = connections.remove(connections.size() - 1);
        }
        return conn;
    }

    public boolean putConnection(Connection conn) {
        synchronized (this) {
            if (connections.size() == limit) {
                logger.warning("Connection pool overfilled");
                return false;
            }

            connections.add(conn);
        }
        return true;
    }

    public int getLimit() {
        return limit;
    }

    private static final ConnectionPool pool = new ConnectionPool("database");

    public static ConnectionPool getInstance() {
        return pool;
    }
}
