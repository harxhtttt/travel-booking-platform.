USE travel_db;

-- 1 Admin, 2 Agents, 3 Travelers
INSERT INTO users (name, email, password_hash, role, active) VALUES 
('System Admin', 'admin@example.com', 'REPLACE_WITH_GENERATED_HASH', 'ADMIN', TRUE),
('Agent Alice', 'alice@travel.com', 'REPLACE_WITH_GENERATED_HASH', 'AGENT', TRUE),
('Agent Bob', 'bob@travel.com', 'REPLACE_WITH_GENERATED_HASH', 'AGENT', TRUE),
('Traveler Tom', 'tom@example.com', 'REPLACE_WITH_GENERATED_HASH', 'TRAVELER', TRUE),
('Traveler Tina', 'tina@example.com', 'REPLACE_WITH_GENERATED_HASH', 'TRAVELER', TRUE),
('Traveler Tim', 'tim@example.com', 'REPLACE_WITH_GENERATED_HASH', 'TRAVELER', TRUE);

-- Listings & Details

-- 4 APPROVED hotels
INSERT INTO listings (agent_id, type, title, price, available_count, status) VALUES 
(2, 'HOTEL', 'Grand Plaza', 150.00, 10, 'APPROVED'),
(2, 'HOTEL', 'Seaside Resort', 250.00, 5, 'APPROVED'),
(3, 'HOTEL', 'Mountain Inn', 120.00, 8, 'APPROVED'),
(3, 'HOTEL', 'City Center Hostel', 40.00, 20, 'APPROVED');

INSERT INTO hotel_details (listing_id, city) VALUES 
(1, 'New York'),
(2, 'Miami'),
(3, 'Denver'),
(4, 'Chicago');

-- 2 APPROVED flights
INSERT INTO listings (agent_id, type, title, price, available_count, status) VALUES 
(2, 'FLIGHT', 'NY to London - Direct', 800.00, 50, 'APPROVED'),
(3, 'FLIGHT', 'Chicago to Denver', 200.00, 30, 'APPROVED');

INSERT INTO flight_details (listing_id, origin, destination, departure_time) VALUES 
(5, 'New York', 'London', '2026-11-01 18:00:00'),
(6, 'Chicago', 'Denver', '2026-11-05 09:30:00');

-- 2 APPROVED cars
INSERT INTO listings (agent_id, type, title, price, available_count, status) VALUES 
(2, 'CAR', 'Compact Car Rental', 45.00, 15, 'APPROVED'),
(3, 'CAR', 'Luxury SUV Rental', 110.00, 4, 'APPROVED');

INSERT INTO car_details (listing_id, pickup_city, car_type) VALUES 
(7, 'New York', 'Compact'),
(8, 'Denver', 'SUV');

-- 2 PENDING hotels
INSERT INTO listings (agent_id, type, title, price, available_count, status) VALUES 
(2, 'HOTEL', 'Desert Oasis Resort', 300.00, 10, 'PENDING'),
(3, 'HOTEL', 'Lakeside Cabins', 95.00, 12, 'PENDING');

INSERT INTO hotel_details (listing_id, city) VALUES 
(9, 'Phoenix'),
(10, 'Seattle');

-- 2 settings
INSERT INTO settings (setting_key, setting_value) VALUES 
('site_name', 'Travel Booking Platform'),
('max_bookings_per_traveler', '10');
