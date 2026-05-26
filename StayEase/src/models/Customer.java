package models;

/**
 * Customer.java
 *
 * Represents a hotel guest.
 * idProof: Aadhar number, Passport, or Driving License
 */
public class Customer {

    private int    customerId;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String idProof;

    // Constructor
    public Customer(int customerId, String name, String phone,
                    String email, String address, String idProof) {
        this.customerId = customerId;
        this.name       = name;
        this.phone      = phone;
        this.email      = email;
        this.address    = address;
        this.idProof    = idProof;
    }

    // Getters
    public int    getCustomerId() { return customerId; }
    public String getName()       { return name;       }
    public String getPhone()      { return phone;      }
    public String getEmail()      { return email;      }
    public String getAddress()    { return address;    }
    public String getIdProof()    { return idProof;    }

    // Setters
    public void setCustomerId(int customerId)  { this.customerId = customerId; }
    public void setName(String name)           { this.name       = name;       }
    public void setPhone(String phone)         { this.phone      = phone;      }
    public void setEmail(String email)         { this.email      = email;      }
    public void setAddress(String address)     { this.address    = address;    }
    public void setIdProof(String idProof)     { this.idProof    = idProof;    }

    @Override
    public String toString() {
        return String.format(
            "ID: %-5d | Name: %-20s | Phone: %-13s | ID Proof: %s",
            customerId, name, phone, idProof
        );
    }
}
