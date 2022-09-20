package ua.hillel.connection;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionProvider {

    public static Connection provideConnection() {
        Properties properties = new Properties();
        Connection connection;

        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/db.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.err.println("property file not found");
            return null;
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/shop", properties);
        } catch (SQLException e) {
            System.err.println("connection failed");
            return null;
        }
        return connection;
    }

    public static void connectionClose(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("Failed to close connection");
        }
    }

}
