# Travel Booking Platform - Design Document

## 1. Functional Requirements

**Admin**
1. Manage users and roles across the platform.
2. Approve or reject new listings submitted by Travel Agents.
3. Manage global system settings.
4. View booking statistics and track pending approvals.

**Travel Agent**
1. Create, update, and manage listings for flights, hotels, and rental cars.
2. Send and receive messages with travelers.
3. Track bookings associated with their managed listings.

**Traveler**
1. Search the platform and book available flights, hotels, and rental cars.
2. View "My Bookings" with real-time status (confirmed or pending).
3. View and manage personal travel itineraries.
4. Send and receive messages with Travel Agents.

---

## 2. Use-Case Diagram

```mermaid
flowchart LR
    %% Actors
    Admin([Admin])
    Agent([Travel Agent])
    Traveler([Traveler])

    %% Admin Use Cases
    Admin --- U1(Manage Users & Roles)
    Admin --- U2(Approve/Reject Listings)
    Admin --- U3(Manage System Settings)
    Admin --- U4(View Booking Statistics)

    %% Agent Use Cases
    Agent --- U5(Create/Manage Listings)
    Agent --- U6(Track Bookings)
    Agent --- U7(Message Travelers)

    %% Traveler Use Cases
    Traveler --- U8(Search & Book Listings)
    Traveler --- U9(View My Bookings)
    Traveler --- U10(Manage Itineraries)
    Traveler --- U7
```

---

## 3. Entity-Relationship (ER) Diagram

```mermaid
erDiagram
    users {
        int id PK
        string name
        string email UK
        string password_hash
        string role "ADMIN, AGENT, TRAVELER"
        boolean active
    }
    settings {
        int id PK
        string setting_key
        string setting_value
    }
    listings {
        int id PK
        int agent_id FK
        string type "FLIGHT, HOTEL, CAR"
        string title
        decimal price
        int available_count
        string status "PENDING, APPROVED, REJECTED"
    }
    flight_details {
        int listing_id PK, FK
        string origin
        string destination
        datetime departure_time
    }
    hotel_details {
        int listing_id PK, FK
        string city
    }
    car_details {
        int listing_id PK, FK
        string pickup_city
        string car_type
    }
    bookings {
        int id PK
        int traveler_id FK
        int listing_id FK
        string status "PENDING, CONFIRMED, CANCELLED"
        date start_date
        date end_date
        datetime booked_at
    }
    itineraries {
        int id PK
        int traveler_id FK
        string name
    }
    itinerary_items {
        int itinerary_id PK, FK
        int booking_id PK, FK
    }
    messages {
        int id PK
        int sender_id FK
        int receiver_id FK
        text content
        datetime sent_at
    }

    users ||--o{ listings : creates
    users ||--o{ bookings : makes
    users ||--o{ itineraries : manages
    users ||--o{ messages : sends
    users ||--o{ messages : receives
    listings ||--o| flight_details : has
    listings ||--o| hotel_details : has
    listings ||--o| car_details : has
    listings ||--o{ bookings : "is_booked_in"
    itineraries ||--o{ itinerary_items : contains
    bookings ||--o{ itinerary_items : "is_part_of"
```

---

## 4. Class Diagram

```mermaid
classDiagram
    class User {
        <<abstract>>
        -int id
        -String name
        -String email
        -String passwordHash
        -String role
        +login()
        +logout()
    }
    class Admin {
        +manageUsers()
        +approveListing()
        +manageSettings()
    }
    class TravelAgent {
        +createListing()
        +trackBookings()
        +sendMessage()
    }
    class Traveler {
        +searchListings()
        +bookListing()
        +manageItinerary()
    }
    
    User <|-- Admin
    User <|-- TravelAgent
    User <|-- Traveler

    class Bookable {
        <<interface>>
        +book(Traveler traveler) boolean
        +checkAvailability() boolean
    }

    class Listing {
        <<abstract>>
        -int id
        -int agentId
        -String type
        -String title
        -double price
        -int availableCount
        -String status
        +calculateTotal() double
    }
    class FlightListing {
        -String origin
        -String destination
        -Date departureTime
    }
    class HotelListing {
        -String city
    }
    class CarListing {
        -String pickupCity
        -String carType
    }

    Listing <|-- FlightListing
    Listing <|-- HotelListing
    Listing <|-- CarListing
    Listing ..|> Bookable

    class TravelException {
        <<Exception>>
    }
    class NoAvailabilityException {
        <<Exception>>
    }
    class InvalidBookingException {
        <<Exception>>
    }
    class UnauthorizedActionException {
        <<Exception>>
    }
    class DataAccessException {
        <<Exception>>
    }

    TravelException <|-- NoAvailabilityException
    TravelException <|-- InvalidBookingException
    TravelException <|-- UnauthorizedActionException
    TravelException <|-- DataAccessException
```

---

## 5. Layered Architecture

1. **Servlet (Controller):** Handles HTTP requests (GET/POST), reads parameters, manages the user Session, calls the Service layer, and forwards data to JSPs.
2. **Service (Business Logic):** Contains core business rules, handles custom exceptions, manages transactions, and deals with multithreading if required.
3. **DAO (Data Access Object):** Executes SQL queries using JDBC `PreparedStatement`, handles result sets, and abstracts away database specifics.
4. **Database (MySQL):** The persistence layer maintaining data integrity and relations.
