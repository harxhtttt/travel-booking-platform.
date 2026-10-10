DROP DATABASE IF EXISTS travel_db;
CREATE DATABASE travel_db;
USE travel_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'AGENT', 'TRAVELER') NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE settings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value VARCHAR(255) NOT NULL
);

CREATE TABLE listings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    agent_id INT NOT NULL,
    type ENUM('FLIGHT', 'HOTEL', 'CAR') NOT NULL,
    title VARCHAR(150) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    available_count INT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    FOREIGN KEY (agent_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE flight_details (
    listing_id INT PRIMARY KEY,
    origin VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time DATETIME NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES listings(id) ON DELETE CASCADE
);

CREATE TABLE hotel_details (
    listing_id INT PRIMARY KEY,
    city VARCHAR(100) NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES listings(id) ON DELETE CASCADE
);

CREATE TABLE car_details (
    listing_id INT PRIMARY KEY,
    pickup_city VARCHAR(100) NOT NULL,
    car_type VARCHAR(100) NOT NULL,
    FOREIGN KEY (listing_id) REFERENCES listings(id) ON DELETE CASCADE
);

CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    traveler_id INT NOT NULL,
    listing_id INT NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    booked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (traveler_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (listing_id) REFERENCES listings(id) ON DELETE CASCADE
);

CREATE TABLE itineraries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    traveler_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    FOREIGN KEY (traveler_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE itinerary_items (
    itinerary_id INT NOT NULL,
    booking_id INT NOT NULL,
    PRIMARY KEY (itinerary_id, booking_id),
    FOREIGN KEY (itinerary_id) REFERENCES itineraries(id) ON DELETE CASCADE,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE
);

CREATE TABLE messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    content TEXT NOT NULL,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
);
