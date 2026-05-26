package models;

/**
 * Room.java
 *
 * Represents a hotel room.
 * roomType  : Single, Double, or Suite
 * price     : per night charge (in rupees)
 * isAvailable: true = vacant, false = occupied
 */
public class Room {

    private int     roomId;
    private String  roomNumber;
    private String  roomType;
    private double  price;
    private boolean isAvailable;

    // Constructor
    public Room(int roomId, String roomNumber, String roomType,
                double price, boolean isAvailable) {
        this.roomId      = roomId;
        this.roomNumber  = roomNumber;
        this.roomType    = roomType;
        this.price       = price;
        this.isAvailable = isAvailable;
    }

    // Getters
    public int     getRoomId()      { return roomId;      }
    public String  getRoomNumber()  { return roomNumber;  }
    public String  getRoomType()    { return roomType;    }
    public double  getPrice()       { return price;       }
    public boolean isAvailable()    { return isAvailable; }

    // Setters
    public void setRoomId(int roomId)           { this.roomId      = roomId;      }
    public void setRoomNumber(String roomNumber){ this.roomNumber  = roomNumber;  }
    public void setRoomType(String roomType)    { this.roomType    = roomType;    }
    public void setPrice(double price)          { this.price       = price;       }
    public void setAvailable(boolean available) { this.isAvailable = available;   }

    @Override
    public String toString() {
        String status = isAvailable ? "Available" : "Occupied";
        return String.format("Room#%-5s | Type: %-8s | Price: Rs.%-8.2f | Status: %s",
                roomNumber, roomType, price, status);
    }
}
