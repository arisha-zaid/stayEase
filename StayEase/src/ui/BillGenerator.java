package ui;

import dao.BookingDAO;
import dao.CustomerDAO;
import dao.RoomDAO;
import models.Booking;
import models.Customer;
import models.Room;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * BillGenerator.java
 *
 * Handles two things:
 *  1. printBill()    → shows a preview bill (customer is still staying)
 *  2. generateFinalBill() → called during checkout, calculates total & prints receipt
 *
 * Bill Calculation:
 *   totalAmount = numberOfNights * roomPricePerNight
 *   (minimum 1 night is charged even for same-day checkout)
 */
public class BillGenerator {

    private BookingDAO  bookingDAO  = new BookingDAO();
    private CustomerDAO customerDAO = new CustomerDAO();
    private RoomDAO     roomDAO     = new RoomDAO();

    // Line separator used in bill formatting
    private static final String LINE = "=".repeat(58);
    private static final String DASH = "-".repeat(58);

    /**
     * Prints a preview bill for an active booking.
     * Does NOT update the database — just shows what the bill WOULD be.
     *
     * @param bookingId the booking to preview
     */
    public void printBill(int bookingId) {

        Booking booking = bookingDAO.getActiveBooking(bookingId);

        if (booking == null) {
            System.out.println("\n[!] No active booking found with ID: " + bookingId);
            System.out.println("    It may have already been checked out, or the ID is wrong.");
            return;
        }

        Customer customer = customerDAO.getCustomerById(booking.getCustomerId());
        Room     room     = roomDAO.getRoomById(booking.getRoomId());

        if (customer == null || room == null) {
            System.out.println("[ERROR] Could not load customer or room details.");
            return;
        }

        // Calculate nights stayed up to today
        LocalDate today   = LocalDate.now();
        long nights = ChronoUnit.DAYS.between(booking.getCheckIn(), today);
        if (nights < 1) nights = 1; // minimum 1 night charge

        double total = nights * room.getPrice();

        printFormattedBill(booking, customer, room, today, nights, total, false);
    }

    /**
     * Calculates the final bill, prints the receipt, and returns the total.
     * Called by AdminMenu during checkout — the total is then saved to DB.
     *
     * @param bookingId the booking being checked out
     * @return total bill amount (to be saved in the booking row)
     */
    public double generateFinalBill(int bookingId) {

        Booking booking = bookingDAO.getActiveBooking(bookingId);

        if (booking == null) {
            System.out.println("\n[!] No active booking found with ID: " + bookingId);
            return -1;
        }

        Customer customer = customerDAO.getCustomerById(booking.getCustomerId());
        Room     room     = roomDAO.getRoomById(booking.getRoomId());

        if (customer == null || room == null) {
            System.out.println("[ERROR] Could not load customer or room details.");
            return -1;
        }

        LocalDate today  = LocalDate.now();
        long nights = ChronoUnit.DAYS.between(booking.getCheckIn(), today);
        if (nights < 1) nights = 1;

        double total = nights * room.getPrice();

        printFormattedBill(booking, customer, room, today, nights, total, true);

        return total;
    }

    /**
     * Private helper — prints the actual formatted bill to console.
     * isFinal: true = FINAL RECEIPT, false = PREVIEW BILL
     */
    private void printFormattedBill(Booking booking, Customer customer,
                                    Room room, LocalDate checkOutDate,
                                    long nights, double total, boolean isFinal) {

        System.out.println("\n" + LINE);
        System.out.println("          STAYEASE HOTEL MANAGEMENT SYSTEM");
        System.out.println(isFinal ? "                    ** FINAL RECEIPT **"
                                   : "                    ** BILL PREVIEW **");
        System.out.println(LINE);

        System.out.printf(" Booking ID   : %d%n",    booking.getBookingId());
        System.out.printf(" Bill Date    : %s%n",    checkOutDate);
        System.out.println(DASH);

        System.out.println(" GUEST DETAILS");
        System.out.printf(" Name         : %s%n",    customer.getName());
        System.out.printf(" Phone        : %s%n",    customer.getPhone());
        System.out.printf(" ID Proof     : %s%n",    customer.getIdProof());
        System.out.println(DASH);

        System.out.println(" ROOM DETAILS");
        System.out.printf(" Room Number  : %s%n",    room.getRoomNumber());
        System.out.printf(" Room Type    : %s%n",    room.getRoomType());
        System.out.printf(" Rate/Night   : Rs. %.2f%n", room.getPrice());
        System.out.println(DASH);

        System.out.println(" STAY DETAILS");
        System.out.printf(" Check-In     : %s%n",    booking.getCheckIn());
        System.out.printf(" Check-Out    : %s%n",    checkOutDate);
        System.out.printf(" Total Nights : %d night(s)%n", nights);
        System.out.println(DASH);

        System.out.println(" CHARGES");
        System.out.printf(" Room Charges : %d x Rs.%.2f = Rs.%.2f%n",
                nights, room.getPrice(), total);
        System.out.println(DASH);
        System.out.printf("                      TOTAL : Rs. %.2f%n", total);
        System.out.println(LINE);

        if (isFinal) {
            System.out.println("   Thank you for staying at StayEase Hotel!");
            System.out.println("           We hope to see you again :)");
            System.out.println(LINE);
        } else {
            System.out.println("  (This is a preview. Actual bill generated at checkout.)");
            System.out.println(LINE);
        }
    }
}
