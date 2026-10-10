# 1. Online Travel Booking Platform
A comprehensive web-based platform to facilitate seamless travel arrangements connecting travelers and agents.
*Java Programming Project, Review 1 (Topic 32)*

## 2. Team ThiccStack
| Member Name | Roll Number |
|---|---|
| Harshit Jha | 25SCSE1011116 |
| Anand Kumar Yadav | 25SCSE1010142 |
| Aryan Yadav | 25SCSE1010769 |
| Shibu Kumar | 25SCSE1011027 |

## 3. Overview and User Roles
The platform centralizes travel booking, allowing seamless interactions between three user types:

| Role | Description |
|---|---|
| Admin | Oversees the platform, manages system settings, and approves/rejects new listings submitted by agents. |
| Travel Agent | Creates and manages listings for hotels, flights, and cars, and monitors their pending/approved status. |
| Traveler | Browses approved listings, searches by keyword, makes bookings, and views booking history. |

## 4. Features
| Working Now | Planned for Review 2 |
|---|---|
| Secure Login & Registration | Complete integration of Flights and Cars searches |
| Role-based Dashboards (AuthFilter) | Custom Itineraries (bundling hotel + flight + car) |
| Hotel Listing Creation (Agent) | In-app Messaging System between travelers and agents |
| Listing Approval Workflow (Admin) | Full-featured analytics on Agent/Admin dashboards |
| Hotel Search & Booking (Traveler) | Broader JUnit test coverage |
| Database Transactions (Commit/Rollback) | Extra feature implementation |

## 5. Technology Stack
- Java 17
- Jakarta Servlets and JSP
- JDBC
- MySQL
- Maven
- Tomcat 10.1 via Cargo
- JUnit 5

## 6. Architecture
Browser ➔ Servlets and AuthFilter ➔ Services ➔ DAOs ➔ MySQL

## 7. Design Diagrams

### Use-Case Diagram
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

### Entity-Relationship Diagram
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

### Class Diagram (Users and Listings)
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
```

### Class Diagram (Exceptions)
```mermaid
classDiagram
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

## 8. Project Structure
```text
src/
├── main/
│   ├── java/com/travel/
│   │   ├── dao/          - Data Access Objects handling database operations
│   │   ├── exception/    - Custom domain exceptions
│   │   ├── filter/       - Servlet filters for role-based authentication
│   │   ├── model/        - Object-oriented entities and enums
│   │   ├── service/      - Core business logic and transaction management
│   │   ├── servlet/      - HTTP request controllers
│   │   └── util/         - Utility classes (DBConnection, Password hashing)
│   ├── resources/
│   │   ├── db/           - Database schema and seed files
│   │   └── db.properties - Database configuration
│   └── webapp/
│       ├── admin/        - JSPs for Admin views
│       ├── agent/        - JSPs for Agent views
│       ├── css/          - Stylesheets
│       ├── traveler/     - JSPs for Traveler views
│       └── WEB-INF/      - Configuration (web.xml) and common JSPs
└── test/
    └── java/com/travel/  - JUnit 5 test classes
```

## 9. Java Concepts Used
| Concept | Classes / Files |
|---|---|
| Object-Oriented Programming (OOP) | `User.java`, `Traveler.java`, `Listing.java` |
| Collections Framework | `ListingDao.java` |
| Multithreading | `BookingService.java` |
| JDBC Transactions (Commit/Rollback) | `BookingService.java` |
| Servlets (Sessions & Role Filter) | `AuthFilter.java`, `LoginServlet.java`, `BookingServlet.java` |

## 10. Database
The application uses a relational schema.

**Tables:** `users`, `settings`, `listings`, `flight_details`, `hotel_details`, `car_details`, `bookings`, `itineraries`, `itinerary_items`, `messages`.

**Creation:**
Execute `schema.sql` to generate the table structure. 
Execute `seed.sql` to populate initial mock data.

## 11. Setup and Run
1. Install JDK 17, Maven, and MySQL.
2. In your MySQL instance, load the database structure and initial data:
   - Run `src/main/resources/db/schema.sql`
   - Run `src/main/resources/db/seed.sql`
3. Navigate to `src/main/resources/` and copy `db.properties.example` to create a new file named `db.properties`. Enter your own MySQL connection details.
4. Run the application from the root directory:
   ```bash
   mvn clean package cargo:run
   ```
5. Open your browser and navigate to `http://localhost:8080/travel-booking-platform/login`.

**Note:** The seed users provided in `seed.sql` do not have working passwords. To try each role:
- Register a new account on the site (new accounts default to Travelers).
- Promote one account to AGENT and another to ADMIN using an SQL statement in your database:
  `UPDATE users SET role='AGENT' WHERE email='your_email';`
  `UPDATE users SET role='ADMIN' WHERE email='another_email';`

## 12. How to Demo
1. Log in as an Agent. Add a new hotel (it will appear as PENDING).
2. Log out and log in as an Admin. Approve the new hotel listing.
3. Log out and log in as a Traveler. Search for the hotel, click book, and view the confirmed booking.

## 13. Tests
Run the JUnit 5 test suite using Maven:
```bash
mvn test
```
The tests cover DAOs checking database retrieval correctness, and Services checking business logic.

## 14. Review 1 Marking Criteria Mapping
| Marking Criterion | Satisfying Files / Classes |
|---|---|
| Problem Understanding and Solution Design | `User.java`, `Listing.java`, `docs/design.md` |
| Core Java Concepts | `Traveler.java`, `BookingService.java`, `ListingDao.java` |
| Database Integration | `BookingService.java`, `ListingDao.java`, `DBConnection.java` |
| Servlets and Web Integration | `LoginServlet.java`, `AuthFilter.java`, `LogoutServlet.java` |

## 15. Planned Next (Review 2)
- flights
- cars
- itineraries
- messaging
- full dashboards
- more tests
- extra feature
