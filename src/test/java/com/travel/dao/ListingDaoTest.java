package com.travel.dao;

import com.travel.model.Listing;
import com.travel.model.ListingStatus;
import com.travel.model.ListingType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ListingDaoTest {

    @Test
    public void testFindApprovedHotels() {
        ListingDao dao = new ListingDao();
        List<Listing> hotels = dao.findByTypeAndStatus(ListingType.HOTEL, ListingStatus.APPROVED);
        
        System.out.println("--- APPROVED HOTELS ---");
        for (Listing hotel : hotels) {
            System.out.println(hotel.getTitle());
        }
        System.out.println("-----------------------");
        
        assertNotNull(hotels);
        assertFalse(hotels.isEmpty(), "Should find some approved hotels!");
    }
}
