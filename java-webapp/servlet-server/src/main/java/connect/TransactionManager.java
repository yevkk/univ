package connect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class TransactionManager {
    private static final Logger logger = Logger.getLogger(TransactionManager.class.getName());

    public static void begin(Connection conn) {
        try {
            conn.prepareStatement("BEGIN TRANSACTION").executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to begin transaction");
        }
    }

    public static void commit(Connection conn) {
        try {
            conn.prepareStatement("COMMIT TRANSACTION").executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to commit transaction");
        }
    }

    public static void rollback(Connection conn) {
        try {
            conn.prepareStatement("ROLLBACK").executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to rollback transaction");
        }
    }
}
