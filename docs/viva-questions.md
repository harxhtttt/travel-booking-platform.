# Review 1 Viva Questions: Travel Booking Platform

1. **Q: How does your application connect to the MySQL database?**
   **A:** We use JDBC with a custom `DBConnection` utility class to establish the connection and manage credentials.

2. **Q: How do you handle different types of users (Admin, Agent, Traveler) in your OOP design?**
   **A:** We use an abstract `User` class containing shared fields (ID, name, email, role), and specific classes like `Traveler` and `Admin` inherit from it.

3. **Q: Can you explain how you prevent unauthorized access to specific dashboards?**
   **A:** We implemented a Servlet Filter (`AuthFilter`) mapped to `/*` that checks the `HttpSession`. It validates if a user object exists and checks their `Role` enum before allowing access to `/admin`, `/agent`, or `/traveler` paths.

4. **Q: How do you ensure data integrity when a traveler books a listing?**
   **A:** We use JDBC Transactions in `BookingService`. We disable auto-commit (`conn.setAutoCommit(false)`), perform the listing availability update and booking record insertion, and then `commit()`. If an exception occurs, we `rollback()` the entire transaction.

5. **Q: Why did you use `PreparedStatement` instead of `Statement` in your DAOs?**
   **A:** `PreparedStatement` prevents SQL injection attacks by escaping inputs and pre-compiling the SQL query for better performance.

6. **Q: How do you handle the availability of a hotel or flight concurrently?**
   **A:** In the `BookingService`, we use a database row-level lock (`SELECT ... FOR UPDATE`) in `findByIdForUpdate()` to ensure that only one thread can book a specific listing at a time if stock is low.

7. **Q: Where and why did you use multithreading in this project?**
   **A:** In `BookingService`, we use an `ExecutorService` to simulate sending a confirmation email asynchronously. This ensures the user doesn't have to wait for the email process to finish before seeing the booking confirmation screen.

8. **Q: How did you model the Listings database schema since Hotels, Flights, and Cars have different attributes?**
   **A:** We used a polymorphic schema approach. A main `listings` table holds common attributes (price, title), and separate tables (`hotel_details`, `flight_details`, `car_details`) hold type-specific attributes, linked by `listing_id`.

9. **Q: What Collection framework interfaces did you use and why?**
   **A:** We primarily used the `List` interface and `ArrayList` implementation in our DAOs (like `ListingDao.findAll()`) to store and return multiple database records predictably, maintaining insertion order.

10. **Q: How do you handle password security in your application?**
    **A:** The passwords are not stored as plain text. The user model stores a `passwordHash`. In a full production app, this would use an algorithm like BCrypt for hashing during registration and verification during login.

11. **Q: Explain the lifecycle of a Servlet in your application.**
    **A:** A Servlet's lifecycle consists of `init()` (called once upon loading), `service()` which delegates to `doGet()` or `doPost()` for each request, and `destroy()` when the server shuts down.

12. **Q: What is the purpose of the Data Access Object (DAO) pattern in your project?**
    **A:** The DAO pattern abstracts and encapsulates all database interactions (SQL queries). This keeps our `Service` layer clean, focused on business logic, and independent of the database implementation.

13. **Q: How are you managing session states for logged-in users?**
    **A:** We use `HttpSession`. When a user logs in successfully via `LoginServlet`, we store the `User` object in the session using `session.setAttribute("user", user)`. We invalidate this session during logout.

14. **Q: What happens if a traveler tries to book a listing that is marked as 'PENDING'?**
    **A:** The `BookingService` throws a custom `NoAvailabilityException` because our validation explicitly checks that the listing's status is `APPROVED` before proceeding with the booking transaction.

15. **Q: What are the main challenges you expect to face in Review 2?**
    **A:** Expanding the search functionality for complex itineraries, managing multi-item transactions safely, and implementing a real-time messaging system using WebSockets or polling.
