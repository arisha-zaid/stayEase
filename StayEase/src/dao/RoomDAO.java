package dao;

import db.DBConnection;
import models.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RoomDAO.java
 *
 * All database operations for the rooms table:
 *   - addRoom()
 *   - viewAllRooms()
 *   - getAvailableRooms()
 *   - getRoomById()
 *   - updateAvailability()
 *   - roomNumberExists()
 */
public class RoomDAO {

    /**
     * Inserts a new room into the rooms table.
     * @return true if insert was successful
     */
    public boolean addRoom(String roomNumber, String roomType, double price) {

        String sql = "INSERT INTO rooms (room_number, room_type, price, is_available) "
                   + "VALUES (?, ?, ?, TRUE)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomNumber);
            stmt.setString(2, roomType);
            stmt.setDouble(3, price);

            int rows = stmt.executeUpdate();
            return rows > 0; // true if at least one row was inserted

        } catch (SQLIntegrityConstraintViolationException e) {
            // Duplicate room number — MySQL throws this specific exception
            System.out.println("[ERROR] Room number '" + roomNumber + "' already exists!");

        } catch (SQLException e) {
            System.out.println("[ERROR] Failed to add room: " + e.getMessage());
        }

        return false;
    }

    /**
     * Returns ALL rooms (available + occupied) for the admin to view.
     */
    public List<Room> viewAllRooms() {

        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms ORDER BY room_number";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price"),
                    rs.getBoolean("is_available")
                );
                rooms.add(room);
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Could not fetch rooms: " + e.getMessage());
        }

        return rooms;
    }

    /**
     * Returns only AVAILABLE rooms — used during booking.
     */
    public List<Room> getAvailableRooms() {

        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE is_available = TRUE ORDER BY room_number";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price"),
                    rs.getBoolean("is_available")
                );
                rooms.add(room);
            }

        } catch (SQLException e) {
            System.out.println("[ERROR] Could not fetch available rooms: " + e.getMessage());
        }

        return rooms;
    }

    /**
     * Fetches a single room by its room_id.
     * Used in BillGenerator to get price per night.
     */
    public Room getRoomById(int roomId) {

        Room room = null;
        String sql = "SELECT * FROM rooms WHERE room_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                room = new Room(
                    rs.getInt("room_id"),
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    rs.getDouble("price"),
                    rs.getBoolean("is_available")
                );
            }
            rs.close();

        } catch (SQLException e) {
            System.out.println("[ERROR] Could not fetch room: " + e.getMessage());
        }

        return room;
    }

    /**
     * Flips is_available for a room.
     * Pass false when booking (room becomes occupied).
     * Pass true when checking out (room becomes available again).
     */
    public boolean updateAvailability(int roomId, boolean available) {

        String sql = "UPDATE rooms SET is_available = ? WHERE room_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, available);
            stmt.setInt(2, roomId);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.out.println("[ERROR] Could not update room availability: " + e.getMessage());
        }

        return false;
    }

    /**
     * Quick check to see if a room_number is already taken.
     */
    public boolean roomNumberExists(String roomNumber) {

        String sql = "SELECT room_id FROM rooms WHERE room_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, roomNumber);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next();
            rs.close();
            return exists;

        } catch (SQLException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }

        return false;
    }
}
