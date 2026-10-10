# Review 1 Report: Travel Booking Platform

**Team Name:** [Team Name]  
**Members:** [Members]  
**Roll Numbers:** [Roll Numbers]  
**Faculty Mentor:** [Faculty Mentor]  

## 1. Introduction
The Travel Booking Platform is a comprehensive web-based application designed to facilitate seamless travel arrangements. It connects travelers with travel agents, allowing for the listing, browsing, and booking of hotels, flights, and rental cars in a single unified interface.

## 2. Requirements
The system serves three primary user roles:
- **Admin**: Oversees the entire platform. Approves or rejects new listings submitted by agents to ensure quality control.
- **Travel Agent**: Creates and manages travel listings (hotels, flights, cars). They can monitor the status of their listings (PENDING, APPROVED, REJECTED).
- **Traveler**: Browses approved listings, searches by keyword or type, and makes bookings for specific dates. They can view their booking history.

## 3. System Design
### Use Case Diagram
The Use Case Diagram illustrates the interactions between the three actors (Admin, Agent, Traveler) and the system.
![Use Case Diagram](screenshots/01-usecase.png)

### Entity-Relationship (ER) Diagram
The ER Diagram models the database schema, showcasing the relationships between users, listings, detailed listing tables (hotel_details, flight_details, car_details) to support polymorphism, and bookings.
![ER Diagram](screenshots/02-er.png)

### Class Diagram
The Class Diagram maps the Object-Oriented design, highlighting inheritance (e.g., User -> Admin, Agent, Traveler), abstract classes, and DAO interfaces.
![Class Diagram](screenshots/03-class.png)

## 4. Technology Stack
- **Backend:** Java 17, Jakarta EE (Servlets, JSP, Filters)
- **Database:** MySQL 8.0, JDBC
- **Frontend:** HTML5, CSS3, JavaScript (JSP for dynamic content)
- **Build Tool:** Maven
- **Testing:** JUnit 5
- **Server:** Apache Tomcat 10+

## 5. Implementation Evidence (Code Snippets)

### Object-Oriented Programming (OOP)
**File:** `src/main/java/com/travel/model/Traveler.java`
```java
package com.travel.model;

/** Shows Inheritance (extends User). */
public class Traveler extends User {
    public Traveler() { super(); }
    public Traveler(int id, String name, String email, String passwordHash) {
        super(id, name, email, passwordHash, Role.TRAVELER);
    }
}
```

### Collections Framework
**File:** `src/main/java/com/travel/dao/ListingDao.java`
```java
    public List<Listing> findByAgent(int agentId) {
        // ... SQL query logic ...
        List<Listing> listings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, agentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) listings.add(mapListing(rs));
            }
        } // ... catch block ...
        return listings;
    }
```

### Multithreading
**File:** `src/main/java/com/travel/service/BookingService.java`
```java
    private void sendConfirmationAsync(Booking booking) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            System.out.println("Async Task: Sending confirmation email for Booking ID " + booking.getId());
        });
        executor.shutdown(); // Properly shut down thread pool
    }
```

### JDBC (Transaction with Commit/Rollback)
**File:** `src/main/java/com/travel/service/BookingService.java`
```java
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // ... database updates ...
                listingDao.decreaseAvailability(listingId, conn);
                bookingDao.save(booking, conn);
                conn.commit();
            } catch (TravelException e) {
                conn.rollback();
                throw e; // Rethrow exact domain exception
            } // ... error handling and finally block with setAutoCommit(true) ...
```

### Servlets (Auth Filter for Role Dashboards)
**File:** `src/main/java/com/travel/filter/AuthFilter.java`
```java
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (path.startsWith("/admin") && (user == null || user.getRole() != Role.ADMIN)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (path.startsWith("/agent") && (user == null || user.getRole() != Role.AGENT)) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }
```

## 6. Traceability Matrix
| Marking Criterion | Satisfying Files / Classes |
| :--- | :--- |
| Problem Understanding and Solution Design | `User.java`, `Listing.java`, `docs/design-preview.html` |
| Core Java Concepts (OOP, Threads, Collections) | `Traveler.java`, `BookingService.java`, `ListingDao.java` |
| Database Integration (JDBC, Transactions) | `BookingService.java`, `ListingDao.java`, `DBConnection.java` |
| Servlets and Web Integration (Sessions, Roles) | `LoginServlet.java`, `AuthFilter.java`, `LogoutServlet.java` |

## 7. Database Initialization (Listings)
Data loaded via `seed.sql`:
| ID | Type | Title | Price | Available | Status | Details |
|---|---|---|---|---|---|---|
| 1 | HOTEL | Grand Plaza | $150.00 | 10 | APPROVED | New York |
| 2 | HOTEL | Seaside Resort | $250.00 | 5 | APPROVED | Miami |
| 3 | HOTEL | Mountain Inn | $120.00 | 8 | APPROVED | Denver |
| 4 | HOTEL | City Center Hostel | $40.00 | 20 | APPROVED | Chicago |
| 5 | FLIGHT | NY to London - Direct | $800.00 | 50 | APPROVED | NY -> London |
| 6 | FLIGHT | Chicago to Denver | $200.00 | 30 | APPROVED | Chicago -> Denver |
| 7 | CAR | Compact Car Rental | $45.00 | 15 | APPROVED | Compact in NY |
| 8 | CAR | Luxury SUV Rental | $110.00 | 4 | APPROVED | SUV in Denver |
| 9 | HOTEL | Desert Oasis Resort | $300.00 | 10 | PENDING | Phoenix |
| 10 | HOTEL | Lakeside Cabins | $95.00 | 12 | PENDING | Seattle |

## 8. Working Features

### 8.1 Login Interface
The secure login page is managed by the `LoginServlet`. It provides a clean form for users to enter their email and password, validating their credentials against the database and routing them to their respective role-based dashboards using the `AuthFilter`.

### 8.2 Agent Dashboard
The Agent Dashboard allows travel agents to manage their listings. Agents can submit new hotels, flights, or car rentals. Newly created listings appear here with a PENDING status, awaiting administrative review.

### 8.3 Admin Dashboard
The Admin Dashboard is a centralized control panel for system administrators. It displays a queue of all PENDING listings submitted by agents. Administrators can review the details and click to APPROVE or REJECT the listings, updating their visibility across the platform.

### 8.4 Traveler Search
The Traveler Search interface empowers users to look up travel options by entering keywords (e.g., 'Jaipur'). It queries the database for APPROVED listings matching the search criteria and displays the available options, prices, and stock counts.

### 8.5 Booking Confirmation
After a traveler selects a listing and specifies their dates, the Booking Confirmation screen shows the finalized reservation in 'My Bookings'. It confirms the transaction was successful and that the asynchronous confirmation email has been queued.

## 9. Testing
Unit testing executed using JUnit via Maven `surefire-plugin`.

**Real `mvn test` output:**
```text
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.travel.dao.ListingDaoTest
--- APPROVED HOTELS ---
Grand Plaza
Seaside Resort
Mountain Inn
City Center Hostel
Desert Oasis Resort
Test Hotel
-----------------------
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.793 s -- in com.travel.dao.ListingDaoTest
[INFO] Running com.travel.service.BookingServiceTest
Async Task: Sending confirmation email for Booking ID 53
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.189 s -- in com.travel.service.BookingServiceTest
[INFO] 
[INFO] Results:
[INFO] 
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

## 10. Limitations and Review 2 Plan
- **Limitations:** Currently missing itinerary bundling, in-app messaging between travelers and agents, and advanced payment gateway integration.
- **Review 2 Scope:** 
  - Complete integration of Flights and Cars searches.
  - Custom Itineraries (bundling hotel + flight + car).
  - Messaging System.
  - Full-featured analytics on Agent/Admin dashboards.
  - Broader JUnit test coverage.

## 11. How to Run
1. Run MySQL server and execute `schema.sql` and `seed.sql`.
2. Update database credentials in `src/main/java/com/travel/util/DBConnection.java`.
3. Run `mvn clean install`.
4. Deploy the generated WAR file to Tomcat 10.
5. Access the application at `http://localhost:8080/travel-booking-platform/login`.
