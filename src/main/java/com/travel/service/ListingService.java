package com.travel.service;

import com.travel.dao.ListingDao;
import com.travel.model.Listing;
import com.travel.model.ListingType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Shows Collections and Collections API usage. */
public class ListingService {
    private final ListingDao listingDao = new ListingDao();

    public List<Listing> searchApproved(ListingType type, String keyword) {
        // Collections: returns a List
        return listingDao.searchApproved(type, keyword);
    }

    public Map<ListingType, List<Listing>> groupByType() {
        List<Listing> all = listingDao.findAll();
        // Collections API: groupingBy stream operation
        return all.stream().collect(Collectors.groupingBy(this::getType));
    }

    private ListingType getType(Listing listing) {
        if (listing instanceof com.travel.model.FlightListing) return ListingType.FLIGHT;
        if (listing instanceof com.travel.model.HotelListing) return ListingType.HOTEL;
        return ListingType.CAR;
    }
}
