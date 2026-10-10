package com.travel.service;

import com.travel.dao.ListingDao;
import com.travel.exception.NoAvailabilityException;
import com.travel.exception.TravelException;
import com.travel.model.Listing;
import com.travel.model.ListingStatus;
import com.travel.model.ListingType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class BookingServiceTest {

    private ListingDao listingDao = new ListingDao();
    private BookingService bookingService = new BookingService();
    private int testListingId;
    private int originalAvailability;

    @BeforeEach
    public void setup() {
        // Find an approved hotel to test with
        List<Listing> hotels = listingDao.findByTypeAndStatus(ListingType.HOTEL, ListingStatus.APPROVED);
        if (hotels.isEmpty()) fail("No approved hotels in database to test with.");
        
        Listing hotel = hotels.get(0);
        testListingId = hotel.getId();
        originalAvailability = hotel.getAvailableCount();
        
        // Set availability to exactly 1 for the test
        hotel.setAvailableCount(1);
        listingDao.update(hotel);
    }

    @AfterEach
    public void teardown() {
        // Restore original availability
        Listing hotel = listingDao.findById(testListingId);
        if (hotel != null) {
            hotel.setAvailableCount(originalAvailability);
            listingDao.update(hotel);
        }
    }

    @Test
    public void testDoubleBookingLastItem() {
        // 1st traveler books the last item
        try {
            bookingService.book(4, testListingId, LocalDate.now().plusDays(1), LocalDate.now().plusDays(5));
        } catch (TravelException e) {
            fail("First booking should have succeeded! " + e.getMessage());
        }

        // 2nd traveler tries to book it immediately after
        assertThrows(NoAvailabilityException.class, () -> {
            bookingService.book(5, testListingId, LocalDate.now().plusDays(2), LocalDate.now().plusDays(6));
        }, "Second booking should fail due to no availability.");
    }
}
