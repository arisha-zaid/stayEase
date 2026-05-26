package models;

/**
 * Admin.java
 *
 * Represents the hotel admin who logs into the system.
 * Simple POJO (Plain Old Java Object) with fields, constructor,
 * getters and setters.
 */
public class Admin {

    private int    adminId;
    private String username;
    private String password;

    // Constructor
    public Admin(int adminId, String username, String password) {
        this.adminId  = adminId;
        this.username = username;
        this.password = password;
    }

    // Getters
    public int    getAdminId()  { return adminId;  }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    // Setters
    public void setAdminId(int adminId)     { this.adminId  = adminId;  }
    public void setUsername(String username){ this.username = username; }
    public void setPassword(String password){ this.password = password; }

    @Override
    public String toString() {
        return "Admin [ID: " + adminId + ", Username: " + username + "]";
    }
}
