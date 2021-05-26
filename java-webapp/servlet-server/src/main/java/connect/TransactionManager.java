package connect;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class TransactionManager {
    private static final Logger logger = Logger.getLogger(TransactionManager.class.getName());

    static void begin(Connection conn) {
        try {
            conn.prepareStatement("BEGIN TRANSACTION").executeUpdate();
        } catch (SQLException e) {
            logger.warning("Failed to begin transaction");
        }
    }

    static void commit(Connection conn) {
        try {
            conn.prepareStatement("COMMIT");
        } catch (SQLException e) {
            logger.warning("Failed to commit transaction");
        }
    }

    static void rollback(Connection conn) {
        try {
            conn.prepareStatement("ROLLBACK");
        } catch (SQLException e) {
            logger.warning("Failed to rollback transaction");
        }
    }
}
