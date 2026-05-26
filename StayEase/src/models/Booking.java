package models;

import java.time.LocalDate;

/**
 * Booking.java
 *
 * Represents a room booking (one row in the bookings table).
 * checkOut and totalAmount are null/0 until the customer checks out.
 */
public class Booking {

    private int        bookingId;
    private int        customerId;
    private int        roomId;
    private LocalDate  checkIn;
    private LocalDate  checkOut;    // null if customer is still staying
    private double     totalAmount; // calculated at checkout
    private String     status;      // "Active" or "Completed"

    // Constructor used when creating a new booking (no checkout yet)
    public Booking(int bookingId, int customerId, int roomId,
                   LocalDate checkIn, String status) {
        this.bookingId  = bookingId;
        this.customerId = customerId;
        this.roomId     = roomId;
        this.checkIn    = checkIn;
        this.status     = status;
    }

    // Full constructor (used when loading from DB after checkout)
    public Booking(int bookingId, int customerId, int roomId,
                   LocalDate checkIn, LocalDate checkOut,
                   double totalAmount, String status) {
        this.bookingId   = bookingId;
        this.customerId  = customerId;
        this.roomId      = roomId;
        this.checkIn     = checkIn;
        this.checkOut    = checkOut;
        this.totalAmount = totalAmount;
        this.status      = status;
    }

    // Getters
    public int       getBookingId()   { return bookingId;   }
    public int       getCustomerId()  { return customerId;  }
    public int       getRoomId()      { return roomId;      }
    public LocalDate getCheckIn()     { return checkIn;     }
    public LocalDate getCheckOut()    { return checkOut;    }
    public double    getTotalAmount() { return totalAmount; }
    public String    getStatus()      { return status;      }

    // Setters
    public void setBookingId(int bookingId)       { this.bookingId   = bookingId;   }
    public void setCustomerId(int customerId)     { this.customerId  = customerId;  }
    public void setRoomId(int roomId)             { this.roomId      = roomId;      }
    public void setCheckIn(LocalDate checkIn)     { this.checkIn     = checkIn;     }
    public void setCheckOut(LocalDate checkOut)   { this.checkOut    = checkOut;    }
    public void setTotalAmount(double totalAmount){ this.totalAmount = totalAmount; }
    public void setStatus(String status)          { this.status      = status;      }

    @Override
    public String toString() {
        String out = (checkOut != null) ? checkOut.toString() : "Still Staying";
        String amt = (totalAmount > 0)  ? String.format("Rs.%.2f", totalAmount) : "Pending";
        return String.format(
            "BookingID: %-5d | CustID: %-5d | RoomID: %-5d | CheckIn: %-12s | CheckOut: %-14s | Amount: %-12s | %s",
            bookingId, customerId, roomId, checkIn, out, amt, status
        );
    }
}
