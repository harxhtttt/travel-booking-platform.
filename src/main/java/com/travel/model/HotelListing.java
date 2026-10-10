package com.travel.model;
import com.travel.exception.NoAvailabilityException;
import com.travel.exception.TravelException;

/** Shows Inheritance and Polymorphism. */
public class HotelListing extends Listing {
    private String city;

    public HotelListing() {}
    public HotelListing(int id, int agentId, String title, double price, int availableCount, ListingStatus status, String city) {
        super(id, agentId, title, price, availableCount, status);
        this.city = city;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    @Override
    public boolean checkAvailability() { 
        return getAvailableCount() > 0 && getStatus() == ListingStatus.APPROVED; 
    }

    @Override
    public boolean book(int travelerId) throws TravelException {
        if (!checkAvailability()) throw new NoAvailabilityException("No rooms available at this hotel.");
        setAvailableCount(getAvailableCount() - 1);
        return true;
    }

    @Override
    public String toString() { 
        return super.toString() + " Hotel{city=" + city + "}"; 
    }
}
