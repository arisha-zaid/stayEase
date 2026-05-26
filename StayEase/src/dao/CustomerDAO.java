package dao;

import db.DBConnection;
import models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerDAO.java
 *
 * All database operations for the customers table:
 *   - addCustomer()    → called during room booking
 *   - getAllCustomers() → view all guests
 *   - getCustomerById() → used in bill / checkout
 */
public class CustomerDAO {

    /**
     * Inserts a new customer and returns the auto-generated customer_id.
     * We need that ID right away to create the booking row.
     *
     * @return the new customer_id, or -1 if insert failed
     */
    public int addCustomer(String name, String phone,
                           String email, String address, String idProof) {

        int generatedId = -1;

        // RETURN_GENERATED_KEYS tells JDBC to give us the auto-increment id
        String sql = "INSERT INTO customers (name, phone, email, address, id_proof) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setString(4, address);
            stmt.setString(5, idProof);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    generatedId = keys.getInt(1); // grab the new ID
                }
                keys.close();
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Could not add customer: " + e.getMessage());
        }

        return generatedId;
    }

    /**
     * Returns all customers — used in "View Customer Details" menu option.
     */
    public List<Customer> getAllCustomers() {

        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY customer_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("id_proof")
                );
                list.add(c);
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Could not fetch customers: " + e.getMessage());
        }

        return list;
    }

    /**
     * Fetches one customer by ID.
     * Used when printing a bill or during checkout.
     */
    public Customer getCustomerById(int customerId) {

        Customer customer = null;
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                customer = new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("address"),
                    rs.getString("id_proof")
                );
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("[ERROR] Could not fetch customer: " + e.getMessage());
        }

        return customer;
    }
}
