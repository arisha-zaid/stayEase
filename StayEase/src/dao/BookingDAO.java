package dao;

import db.DBConnection;
import models.Booking;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * BookingDAO.java
 *
 * All database operations for the bookings table:
 *   - createBooking()      → save a new booking when a room is booked
 *   - getActiveBooking()   → find current booking by booking ID
 *   - checkout()           → set check_out date, amount, status = Completed
 *   - viewAllBookings()    → full history for the admin
 *   - hasActiveBooking()   → check if a room is already booked
 */
public class BookingDAO {

    /**
     * Creates a new booking row in the database.
     * check_out and total_amount are left null — filled at checkout.
     *
     * @return the new booking_id, or -1 if insert failed
     */
    public int createBooking(int customerId, int roomId, LocalDate checkIn) {

        int generatedId = -1;

        String sql = "INSERT INTO bookings (customer_id, room_id, check_in, status) "
                   + "VALUES (?, ?, ?, 'Active')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, customerId);
            stmt.setInt(2, roomId);
            stmt.setDate(3, Date.valueOf(checkIn)); // LocalDate → java.sql.Date

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    generatedId = keys.getInt(1);
                }
                keys.close();
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Could not create booking: " + e.getMessage());
        }

        return generatedId;
    }

    /**
     * Fetches an ACTIVE booking by its booking_id.
     * Used in generate-bill and checkout flows.
     * Returns null if not found or already completed.
     */
    public Booking getActiveBooking(int bookingId) {

        Booking booking = null;

        String sql = "SELECT * FROM bookings WHERE booking_id = ? AND status = 'Active'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // check_out is null for active bookings, so we handle that safely
                Date checkOutDate = rs.getDate("check_out");
                LocalDate checkOut = (checkOutDate != null) ? checkOutDate.toLocalDate() : null;

                booking = new Booking(
                    rs.getInt("booking_id"),
                    rs.getInt("customer_id"),
                    rs.getInt("room_id"),
                    rs.getDate("check_in").toLocalDate(),
                    checkOut,
                    rs.getDouble("total_amount"),
                    rs.getString("status")
                );
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("[ERROR] Could not fetch booking: " + e.getMessage());
        }

        return booking;
    }

    /**
     * Completes the checkout:
     *  - sets check_out to today
     *  - sets total_amount
     *  - changes status to 'Completed'
     *
     * @return true if update was successful
     */
    public boolean checkout(int bookingId, LocalDate checkOut, double totalAmount) {

        String sql = "UPDATE bookings "
                   + "SET check_out = ?, total_amount = ?, status = 'Completed' "
                   + "WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(checkOut));
            stmt.setDouble(2, totalAmount);
            stmt.setInt(3, bookingId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("[ERROR] Checkout failed: " + e.getMessage());
        }

        return false;
    }

    /**
     * Returns ALL bookings (active + completed) for the history view.
     * Joins with customers and rooms tables to show names and room numbers
     * instead of just IDs — much more readable.
     */
    public List<String> viewAllBookings() {

        List<String> history = new ArrayList<>();

        // A JOIN query so we display customer name and room number directly
        String sql = "SELECT b.booking_id, c.name, r.room_number, r.room_type, "
                   + "       b.check_in, b.check_out, b.total_amount, b.status "
                   + "FROM bookings b "
                   + "JOIN customers c ON b.customer_id = c.customer_id "
                   + "JOIN rooms     r ON b.room_id     = r.room_id "
                   + "ORDER BY b.booking_id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String checkOut = (rs.getDate("check_out") != null)
                        ? rs.getDate("check_out").toString()
                        : "Still Staying";

                String amount = (rs.getDouble("total_amount") > 0)
                        ? String.format("Rs.%.2f", rs.getDouble("total_amount"))
                        : "Pending";

                String row = String.format(
                    "ID:%-4d | %-20s | Room:%-5s (%-7s) | In:%-12s | Out:%-14s | %-12s | %s",
                    rs.getInt("booking_id"),
                    rs.getString("name"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDate("check_in").toString(),
                    checkOut,
                    amount,
                    rs.getString("status")
                );
                history.add(row);
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Could not fetch booking history: " + e.getMessage());
        }

        return history;
    }

    /**
     * Checks if a room already has an active booking.
     * Prevents double-booking the same room.
     */
    public boolean hasActiveBooking(int roomId) {

        String sql = "SELECT booking_id FROM bookings "
                   + "WHERE room_id = ? AND status = 'Active'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            boolean found = rs.next();
            rs.close();
            return found;

        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        return false;
    }
}
