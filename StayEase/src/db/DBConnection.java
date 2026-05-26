package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection.java
 *
 * A simple helper class that gives us a MySQL connection.
 * We call DBConnection.getConnection() from every DAO class.
 *
 * IMPORTANT: Change DB_PASSWORD to your MySQL root password.
 */
public class DBConnection {

    private static final String DB_URL      = "jdbc:mysql://localhost:3306/stayease";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "your_password";  // <-- change this

    // Returns a live Connection object, or null if something goes wrong
    public static Connection getConnection() {

        Connection connection = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Try to connect to the database
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        } catch (ClassNotFoundException e) {
            System.out.println("\n[ERROR] MySQL Driver not found!");
            System.out.println("Make sure mysql-connector-j-x.x.xx.jar is inside the lib/ folder");
            System.out.println("and is added to the classpath when compiling/running.\n");

        } catch (SQLException e) {
            System.out.println("\n[ERROR] Could not connect to the database!");
            System.out.println("Check: Is MySQL running? Is the password correct?");
            System.out.println("Details: " + e.getMessage() + "\n");
        }

        return connection;
    }
}
