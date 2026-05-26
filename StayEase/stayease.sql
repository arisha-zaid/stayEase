-- ==========================================================
--  StayEase Hotel Management System
--  Database Setup Script
--  Run this file once before starting the application.
--  Command: mysql -u root -p < stayease.sql
-- ==========================================================

CREATE DATABASE IF NOT EXISTS stayease;
USE stayease;

-- ----------------------------------------------------------
-- Table 1: admin
-- Stores login credentials for hotel admin
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS admin (
    admin_id   INT PRIMARY KEY AUTO_INCREMENT,
    username   VARCHAR(50) NOT NULL UNIQUE,
    password   VARCHAR(50) NOT NULL
);

-- Default admin account  -->  username: admin  |  password: admin123
INSERT INTO admin (username, password)
VALUES ('admin', 'admin123')
ON DUPLICATE KEY UPDATE username = username;


-- ----------------------------------------------------------
-- Table 2: rooms
-- All hotel rooms with type, price, and availability
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS rooms (
    room_id       INT PRIMARY KEY AUTO_INCREMENT,
    room_number   VARCHAR(10)                       NOT NULL UNIQUE,
    room_type     ENUM('Single', 'Double', 'Suite')  NOT NULL,
    price         DECIMAL(8, 2)                     NOT NULL,
    is_available  BOOLEAN                           DEFAULT TRUE
);

-- A few sample rooms to get started
INSERT INTO rooms (room_number, room_type, price, is_available) VALUES
('101', 'Single', 1200.00, TRUE),
('102', 'Single', 1200.00, TRUE),
('201', 'Double', 2200.00, TRUE),
('202', 'Double', 2200.00, TRUE),
('301', 'Suite',  4500.00, TRUE)
ON DUPLICATE KEY UPDATE room_number = room_number;


-- ----------------------------------------------------------
-- Table 3: customers
-- Guest information collected at the time of booking
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS customers (
    customer_id  INT PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(100) NOT NULL,
    phone        VARCHAR(15)  NOT NULL,
    email        VARCHAR(100),
    address      TEXT,
    id_proof     VARCHAR(50)
);


-- ----------------------------------------------------------
-- Table 4: bookings
-- Links a customer to a room for a date range
-- status: 'Active' while staying, 'Completed' after checkout
-- ----------------------------------------------------------
CREATE TABLE IF NOT EXISTS bookings (
    booking_id    INT PRIMARY KEY AUTO_INCREMENT,
    customer_id   INT           NOT NULL,
    room_id       INT           NOT NULL,
    check_in      DATE          NOT NULL,
    check_out     DATE,
    total_amount  DECIMAL(10,2),
    status        ENUM('Active','Completed') DEFAULT 'Active',

    FOREIGN KEY (customer_id) REFERENCES customers(customer_id),
    FOREIGN KEY (room_id)     REFERENCES rooms(room_id)
);

-- ==========================================================
--  Done! Default login --> admin / admin123
-- ==========================================================
