package ui;

import dao.BookingDAO;
import dao.CustomerDAO;
import dao.RoomDAO;
import models.Booking;
import models.Customer;
import models.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * AdminMenu.java
 *
 * The main dashboard after a successful login.
 * Runs in a loop until the admin chooses to logout.
 *
 * Menu Options:
 *   1. Add Room
 *   2. View All Rooms
 *   3. Book Room
 *   4. View Customer Details
 *   5. Generate Bill (Preview)
 *   6. Checkout Customer
 *   7. View Booking History
 *   8. Logout
 */
public class AdminMenu {

    private Scanner     scanner;
    private RoomDAO     roomDAO     = new RoomDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private BookingDAO  bookingDAO  = new BookingDAO();
    private BillGenerator billGen   = new BillGenerator();

    public AdminMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    // Show the dashboard loop
    public void show() {

        boolean running = true;

        while (running) {
            printMenuHeader();

            System.out.print(" Enter your choice: ");
            String input = scanner.nextLine().trim();

            // Handle non-numeric input gracefully
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("\n[!] Please enter a number between 1 and 8.\n");
                continue;
            }

            switch (choice) {
                case 1: addRoom();            break;
                case 2: viewRooms();          break;
                case 3: bookRoom();           break;
                case 4: viewCustomers();      break;
                case 5: generateBillPreview();break;
                case 6: checkoutCustomer();   break;
                case 7: viewBookingHistory(); break;
                case 8:
                    System.out.println("\n Logging out... Goodbye!\n");
                    running = false;
                    break;
                default:
                    System.out.println("\n[!] Invalid choice. Please enter 1-8.\n");
            }
        }
    }

    // ----------------------------------------------------------
    //  Print the menu header
    // ----------------------------------------------------------
    private void printMenuHeader() {
        System.out.println("\n" + "=".repeat(45));
        System.out.println("       STAYEASE - ADMIN DASHBOARD");
        System.out.println("=".repeat(45));
        System.out.println("  1. Add Room");
        System.out.println("  2. View All Rooms");
        System.out.println("  3. Book Room");
        System.out.println("  4. View Customer Details");
        System.out.println("  5. Generate Bill (Preview)");
        System.out.println("  6. Checkout Customer");
        System.out.println("  7. View Booking History");
        System.out.println("  8. Logout");
        System.out.println("-".repeat(45));
    }

    // ----------------------------------------------------------
    //  1. Add Room
    // ----------------------------------------------------------
    private void addRoom() {
        System.out.println("\n--- ADD NEW ROOM ---");

        System.out.print(" Room Number (e.g. 104): ");
        String roomNumber = scanner.nextLine().trim();

        if (roomNumber.isEmpty()) {
            System.out.println("[!] Room number cannot be empty.");
            return;
        }

        // Check if room number already exists before asking more questions
        if (roomDAO.roomNumberExists(roomNumber)) {
            System.out.println("[!] Room number '" + roomNumber + "' already exists.");
            return;
        }

        System.out.println(" Room Type:");
        System.out.println("   1. Single  (basic single bed)");
        System.out.println("   2. Double  (two beds or king size)");
        System.out.println("   3. Suite   (luxury suite)");
        System.out.print(" Choose type (1/2/3): ");
        String typeChoice = scanner.nextLine().trim();

        String roomType;
        switch (typeChoice) {
            case "1": roomType = "Single"; break;
            case "2": roomType = "Double"; break;
            case "3": roomType = "Suite";  break;
            default:
                System.out.println("[!] Invalid room type. Room not added.");
                return;
        }

        System.out.print(" Price per night (Rs.): ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine().trim());
            if (price <= 0) {
                System.out.println("[!] Price must be greater than zero.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid price entered.");
            return;
        }

        boolean success = roomDAO.addRoom(roomNumber, roomType, price);

        if (success) {
            System.out.println("\n[SUCCESS] Room " + roomNumber
                    + " (" + roomType + ") added at Rs." + price + "/night.");
        } else {
            System.out.println("[FAILED] Could not add room. Please try again.");
        }
    }

    // ----------------------------------------------------------
    //  2. View All Rooms
    // ----------------------------------------------------------
    private void viewRooms() {
        System.out.println("\n--- ALL ROOMS ---");
        System.out.println("-".repeat(65));

        List<Room> rooms = roomDAO.viewAllRooms();

        if (rooms.isEmpty()) {
            System.out.println(" No rooms found. Add rooms first.");
        } else {
            int available = 0, occupied = 0;
            for (Room r : rooms) {
                System.out.println("  " + r.toString());
                if (r.isAvailable()) available++; else occupied++;
            }
            System.out.println("-".repeat(65));
            System.out.printf("  Total: %d  |  Available: %d  |  Occupied: %d%n",
                    rooms.size(), available, occupied);
        }
        System.out.println("-".repeat(65));
    }

    // ----------------------------------------------------------
    //  3. Book Room
    // ----------------------------------------------------------
    private void bookRoom() {
        System.out.println("\n--- BOOK A ROOM ---");

        // Step 1: Show available rooms
        List<Room> available = roomDAO.getAvailableRooms();

        if (available.isEmpty()) {
            System.out.println(" Sorry, no rooms are available right now.");
            return;
        }

        System.out.println(" Available Rooms:");
        System.out.println("-".repeat(60));
        for (Room r : available) {
            System.out.println("  [ID: " + r.getRoomId() + "] " + r.toString());
        }
        System.out.println("-".repeat(60));

        // Step 2: Choose a room
        System.out.print(" Enter Room ID to book: ");
        int roomId;
        try {
            roomId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid room ID.");
            return;
        }

        // Verify the room ID is actually in the available list
        Room selectedRoom = null;
        for (Room r : available) {
            if (r.getRoomId() == roomId) {
                selectedRoom = r;
                break;
            }
        }

        if (selectedRoom == null) {
            System.out.println("[!] Room ID not found or not available.");
            return;
        }

        System.out.println(" Selected: " + selectedRoom.toString());

        // Step 3: Collect customer details
        System.out.println("\n Enter Guest Details:");
        System.out.print(" Full Name     : ");
        String name = scanner.nextLine().trim();

        System.out.print(" Phone Number  : ");
        String phone = scanner.nextLine().trim();

        System.out.print(" Email         : ");
        String email = scanner.nextLine().trim();

        System.out.print(" Address       : ");
        String address = scanner.nextLine().trim();

        System.out.print(" ID Proof      : ");
        String idProof = scanner.nextLine().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            System.out.println("[!] Name and phone are required.");
            return;
        }

        // Step 4: Save customer to DB
        int customerId = customerDAO.addCustomer(name, phone, email, address, idProof);

        if (customerId == -1) {
            System.out.println("[ERROR] Could not save customer details.");
            return;
        }

        // Step 5: Create the booking (check-in = today)
        LocalDate today = LocalDate.now();
        int bookingId = bookingDAO.createBooking(customerId, roomId, today);

        if (bookingId == -1) {
            System.out.println("[ERROR] Could not create booking.");
            return;
        }

        // Step 6: Mark the room as occupied
        roomDAO.updateAvailability(roomId, false);

        // Step 7: Confirm
        System.out.println("\n" + "=".repeat(50));
        System.out.println("   BOOKING CONFIRMED!");
        System.out.printf("   Booking ID   : %d%n", bookingId);
        System.out.printf("   Guest Name   : %s%n", name);
        System.out.printf("   Room         : %s (%s)%n",
                selectedRoom.getRoomNumber(), selectedRoom.getRoomType());
        System.out.printf("   Check-In     : %s%n", today);
        System.out.printf("   Rate/Night   : Rs. %.2f%n", selectedRoom.getPrice());
        System.out.println("   (Save the Booking ID for checkout)");
        System.out.println("=".repeat(50));
    }

    // ----------------------------------------------------------
    //  4. View Customer Details
    // ----------------------------------------------------------
    private void viewCustomers() {
        System.out.println("\n--- ALL CUSTOMERS ---");
        System.out.println("-".repeat(75));

        List<Customer> customers = customerDAO.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println(" No customers found yet.");
        } else {
            for (Customer c : customers) {
                System.out.println("  " + c.toString());
                System.out.printf("       Email: %-30s | Address: %s%n",
                        c.getEmail() != null ? c.getEmail() : "N/A",
                        c.getAddress() != null ? c.getAddress() : "N/A");
                System.out.println("  " + "-".repeat(71));
            }
        }
        System.out.println("-".repeat(75));
    }

    // ----------------------------------------------------------
    //  5. Generate Bill (Preview)
    // ----------------------------------------------------------
    private void generateBillPreview() {
        System.out.println("\n--- GENERATE BILL PREVIEW ---");
        System.out.print(" Enter Booking ID: ");

        int bookingId;
        try {
            bookingId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid booking ID.");
            return;
        }

        billGen.printBill(bookingId);
    }

    // ----------------------------------------------------------
    //  6. Checkout Customer
    // ----------------------------------------------------------
    private void checkoutCustomer() {
        System.out.println("\n--- CHECKOUT CUSTOMER ---");
        System.out.print(" Enter Booking ID to checkout: ");

        int bookingId;
        try {
            bookingId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[!] Invalid booking ID.");
            return;
        }

        // Fetch the booking first to confirm it's active
        Booking booking = bookingDAO.getActiveBooking(bookingId);

        if (booking == null) {
            System.out.println("[!] No active booking found with ID: " + bookingId);
            System.out.println("    It may already be checked out.");
            return;
        }

        // Confirm before proceeding
        System.out.print("\n Are you sure you want to checkout Booking #"
                + bookingId + "? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (!confirm.equals("yes") && !confirm.equals("y")) {
            System.out.println(" Checkout cancelled.");
            return;
        }

        // Generate final bill and get total amount
        double totalAmount = billGen.generateFinalBill(bookingId);

        if (totalAmount < 0) {
            System.out.println("[ERROR] Could not generate bill. Checkout cancelled.");
            return;
        }

        // Update booking row: set check_out, total_amount, status = Completed
        LocalDate today = LocalDate.now();
        boolean updated = bookingDAO.checkout(bookingId, today, totalAmount);

        if (updated) {
            // Free up the room
            roomDAO.updateAvailability(booking.getRoomId(), true);
            System.out.println("\n[SUCCESS] Checkout complete. Room is now available.");
        } else {
            System.out.println("[ERROR] Could not update booking. Please try again.");
        }
    }

    // ----------------------------------------------------------
    //  7. View Booking History
    // ----------------------------------------------------------
    private void viewBookingHistory() {
        System.out.println("\n--- BOOKING HISTORY ---");
        System.out.println("-".repeat(110));

        List<String> history = bookingDAO.viewAllBookings();

        if (history.isEmpty()) {
            System.out.println(" No bookings found.");
        } else {
            for (String row : history) {
                System.out.println("  " + row);
            }
            System.out.println("-".repeat(110));
            System.out.println("  Total records: " + history.size());
        }
        System.out.println("-".repeat(110));
    }
}
