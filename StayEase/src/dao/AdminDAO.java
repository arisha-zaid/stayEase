package dao;

import db.DBConnection;
import models.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AdminDAO.java
 *
 * Handles all database operations related to the admin table.
 * Right now we only need login, but you could add
 * changePassword() here later.
 */
public class AdminDAO {

    /**
     * Checks if username + password match a row in the admin table.
     *
     * @return Admin object if credentials are valid, null otherwise
     */
    public Admin login(String username, String password) {

        Admin admin = null;

        // We use a PreparedStatement to avoid SQL injection
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";

        // try-with-resources automatically closes Connection and PreparedStatement
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Credentials matched — build an Admin object from the result
                admin = new Admin(
                    rs.getInt("admin_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("[ERROR] Login query failed: " + e.getMessage());
        }

        return admin; // null means login failed
    }
}
