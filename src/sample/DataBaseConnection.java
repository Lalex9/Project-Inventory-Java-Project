package sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBaseConnection {
    private static Connection connection;
    private static boolean hasConnectionActive = false;

    public static void initializeConnection() {
        connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://remotemysql.com:3306/lH9n6CqLEK",
                    "lH9n6CqLEK", "piitLnEzuR");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(connection != null) {
            hasConnectionActive = true;
        }
    }

    public static Connection getConnection() {
        if(hasConnectionActive) {
            return connection;
        } else {
            return null;
        }
    }

    private DataBaseConnection() {
    }

    public static void endConnection() {
        if(hasConnectionActive) {
            try {
                connection.close();
                hasConnectionActive = false;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
