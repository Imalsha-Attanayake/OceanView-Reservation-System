CREATE DATABASE oceanview_booking;
USE oceanview_booking;
USE oceanview_booking;

CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL,
    room_type VARCHAR(50),
    price DECIMAL(10,2),
    availability BOOLEAN
);

CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE reservations (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    guest_name VARCHAR(100) NOT NULL,
    contact_number VARCHAR(20),
    room_id INT,
    check_in_date DATE,
    check_out_date DATE,
    total_bill DECIMAL(10,2),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);

SHOW TABLES;

USE oceanview_booking;

INSERT INTO users (username, password)
VALUES ('admin', 'admin123');

USE oceanview_booking;

INSERT INTO rooms (room_number, room_type, price, availability)
VALUES
('101', 'Single', 5000.00, true),
('102', 'Double', 7500.00, true),
('201', 'Deluxe', 12000.00, true),
('202', 'Suite', 20000.00, false);

SELECT * FROM rooms;