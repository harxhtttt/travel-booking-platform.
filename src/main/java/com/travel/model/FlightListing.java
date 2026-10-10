package com.travel.model;
import java.time.LocalDateTime;
import com.travel.exception.NoAvailabilityException;
import com.travel.exception.TravelException;

/** Shows Inheritance and Polymorphism. */
public class FlightListing extends Listing {
    private String origin;
    private String destination;
    private LocalDateTime departureTime;

    public FlightListing() {}
    public FlightListing(int id, int agentId, String title, double price, int availableCount, ListingStatus status, String origin, String destination, LocalDateTime departureTime) {
        super(id, agentId, title, price, availableCount, status);
        this.origin = origin; 
        this.destination = destination; 
        this.departureTime = departureTime;
    }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    @Override
    public boolean checkAvailability() { 
        return getAvailableCount() > 0 && getStatus() == ListingStatus.APPROVED; 
    }

    @Override
    public boolean book(int travelerId) throws TravelException {
        if (!checkAvailability()) throw new NoAvailabilityException("Flight is fully booked or unavailable.");
        setAvailableCount(getAvailableCount() - 1);
        return true;
    }

    @Override
    public String toString() { 
        return super.toString() + " Flight{from=" + origin + ", to=" + destination + "}"; 
    }
}
