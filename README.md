# Travel Booking Platform

A university Java project built with Java 17, Maven, Jakarta Servlets 6, JSP, and MySQL 8.
Does not use Spring or Hibernate frameworks.

## Requirements
- JDK 17
- Maven 3+
- MySQL 8

## How to Run Locally

You do not need to install Tomcat manually. We use the Cargo Maven plugin to run Tomcat 10.1 embedded.

1. Open your terminal and navigate to this project's root folder (`travel-booking-platform`).
2. Run the following command:
   ```bash
   mvn clean package cargo:run
   ```
3. Open your browser and go to: `http://localhost:8080/travel-booking-platform`

To stop the server, press `Ctrl + C` in the terminal.
