package com.travel.service;

import com.travel.dao.BookingDao;
import com.travel.dao.ListingDao;
import com.travel.exception.InvalidBookingException;
import com.travel.exception.NoAvailabilityException;
import com.travel.exception.TravelException;
import com.travel.model.Booking;
import com.travel.model.BookingStatus;
import com.travel.model.Listing;
import com.travel.model.ListingStatus;
import com.travel.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Shows Transactions, Multithreading, Exceptions, and OOP. */
public class BookingService {
    private final ListingDao listingDao = new ListingDao();
    private final BookingDao bookingDao = new BookingDao();

    public void book(int travelerId, int listingId, LocalDate startDate, LocalDate endDate) throws TravelException {
        // OOP: Validation logic encapsulated here
        if (startDate == null || endDate == null || endDate.isBefore(startDate) || startDate.isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Invalid booking dates provided."); // Exceptions
        }

        // Transactions
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // Thread-safety via database row lock (FOR UPDATE)
                Listing listing = listingDao.findByIdForUpdate(listingId, conn);
                if (listing == null || listing.getStatus() != ListingStatus.APPROVED || listing.getAvailableCount() <= 0) {
                    throw new NoAvailabilityException("Listing is not available for booking.");
                }

                // Call polymorphic book method which throws NoAvailabilityException if count is 0
                listing.book(travelerId); 

                // Execute db changes
                listingDao.decreaseAvailability(listingId, conn);
                
                Booking booking = new Booking(0, travelerId, listingId, BookingStatus.CONFIRMED, startDate, endDate, LocalDateTime.now());
                bookingDao.save(booking, conn);

                conn.commit();

                // Multithreading
                sendConfirmationAsync(booking);

            } catch (TravelException e) {
                conn.rollback();
                throw e; // Rethrow exact domain exception
            } catch (SQLException e) {
                conn.rollback();
                throw new TravelException("Booking failed due to DB error", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new TravelException("Database error during booking", e);
        }
    }

    public void cancel(int bookingId, int travelerId) throws TravelException {
        // Transactions
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Booking booking = bookingDao.findById(bookingId);
                if (booking == null || booking.getTravelerId() != travelerId || booking.getStatus() == BookingStatus.CANCELLED) {
                    throw new InvalidBookingException("Cannot cancel this booking.");
                }

                booking.setStatus(BookingStatus.CANCELLED);
                bookingDao.update(booking);
                
                listingDao.increaseAvailability(booking.getListingId(), conn);
                
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw new TravelException("Cancellation failed", e);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new TravelException("Database error during cancellation", e);
        }
    }

    private void sendConfirmationAsync(Booking booking) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            System.out.println("Async Task: Sending confirmation email for Booking ID " + booking.getId());
        });
        executor.shutdown(); // Properly shut down thread pool
    }
}
