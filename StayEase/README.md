# StayEase – Hotel Management System

A beginner-to-intermediate Java console application built with Core Java, JDBC, and MySQL.

---

## Project Structure

```
StayEase/
├── src/
│   ├── main/Main.java           <- Entry point
│   ├── db/DBConnection.java     <- MySQL connection helper
│   ├── models/
│   │   ├── Admin.java
│   │   ├── Room.java
│   │   ├── Customer.java
│   │   └── Booking.java
│   ├── dao/
│   │   ├── AdminDAO.java
│   │   ├── RoomDAO.java
│   │   ├── CustomerDAO.java
│   │   └── BookingDAO.java
│   └── ui/
│       ├── MainMenu.java
│       ├── AdminMenu.java
│       └── BillGenerator.java
├── lib/
│   └── mysql-connector-j-x.x.xx.jar  <- Add this manually!
├── stayease.sql
└── README.md
```

---

## Setup Instructions

### Step 1 – Database Setup
1. Open MySQL Workbench (or terminal)
2. Run the SQL script:
   ```
   mysql -u root -p < stayease.sql
   ```
   This creates the database, all 4 tables, and inserts a default admin.

### Step 2 – Set Your Password
Open `src/db/DBConnection.java` and change:
```java
private static final String DB_PASSWORD = "your_password";
```
to your actual MySQL root password.

### Step 3 – Download the MySQL JDBC Driver
- Download from: https://dev.mysql.com/downloads/connector/j/
- Choose "Platform Independent" → download the ZIP
- Extract and copy `mysql-connector-j-x.x.xx.jar` into the `lib/` folder

### Step 4 – Compile

**Windows:**
```
javac -cp "lib\mysql-connector-j-8.x.xx.jar" -d out src\db\*.java src\models\*.java src\dao\*.java src\ui\*.java src\main\Main.java
```

**Mac/Linux:**
```
javac -cp "lib/mysql-connector-j-8.x.xx.jar" -d out src/db/*.java src/models/*.java src/dao/*.java src/ui/*.java src/main/Main.java
```

### Step 5 – Run

**Windows:**
```
java -cp "out;lib\mysql-connector-j-8.x.xx.jar" main.Main
```

**Mac/Linux:**
```
java -cp "out:lib/mysql-connector-j-8.x.xx.jar" main.Main
```

---

## Default Login
```
Username: admin
Password: admin123
```

---

## Features
| # | Feature | Description |
|---|---------|-------------|
| 1 | Admin Login | Secure login with 3-attempt limit |
| 2 | Add Room | Add Single / Double / Suite rooms |
| 3 | View Rooms | See all rooms with availability status |
| 4 | Book Room | Collect guest info and book a room |
| 5 | Customer Details | View all registered guests |
| 6 | Generate Bill | Preview bill for an active stay |
| 7 | Checkout | Generate final bill and free the room |
| 8 | Booking History | View all past and current bookings |

---

## Tech Stack
- Java 8+
- JDBC (no ORM)
- MySQL
- No Spring, no Maven, no frameworks
