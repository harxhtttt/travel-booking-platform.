package com.travel.model;
import com.travel.exception.NoAvailabilityException;
import com.travel.exception.TravelException;

/** Shows Inheritance and Polymorphism. */
public class CarListing extends Listing {
    private String pickupCity;
    private String carType;

    public CarListing() {}
    public CarListing(int id, int agentId, String title, double price, int availableCount, ListingStatus status, String pickupCity, String carType) {
        super(id, agentId, title, price, availableCount, status);
        this.pickupCity = pickupCity; 
        this.carType = carType;
    }

    public String getPickupCity() { return pickupCity; }
    public void setPickupCity(String pickupCity) { this.pickupCity = pickupCity; }
    public String getCarType() { return carType; }
    public void setCarType(String carType) { this.carType = carType; }

    @Override
    public boolean checkAvailability() { 
        return getAvailableCount() > 0 && getStatus() == ListingStatus.APPROVED; 
    }

    @Override
    public boolean book(int travelerId) throws TravelException {
        if (!checkAvailability()) throw new NoAvailabilityException("No cars of this type available.");
        setAvailableCount(getAvailableCount() - 1);
        return true;
    }

    @Override
    public String toString() { 
        return super.toString() + " Car{type=" + carType + ", pickup=" + pickupCity + "}"; 
    }
}
