package com.travel.model;

/** Shows Abstraction and Encapsulation. Implements Bookable Interface. */
public abstract class Listing implements Bookable {
    private int id;
    private int agentId;
    private String title;
    private double price;
    private int availableCount;
    private ListingStatus status;

    public Listing() {}
    public Listing(int id, int agentId, String title, double price, int availableCount, ListingStatus status) {
        this.id = id; 
        this.agentId = agentId; 
        this.title = title; 
        this.price = price; 
        this.availableCount = availableCount; 
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAgentId() { return agentId; }
    public void setAgentId(int agentId) { this.agentId = agentId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getAvailableCount() { return availableCount; }
    public void setAvailableCount(int availableCount) { this.availableCount = availableCount; }
    public ListingStatus getStatus() { return status; }
    public void setStatus(ListingStatus status) { this.status = status; }

    public String getTypeLabel() {
        if (this instanceof HotelListing) return "HOTEL";
        if (this instanceof FlightListing) return "FLIGHT";
        if (this instanceof CarListing) return "CAR";
        return "UNKNOWN";
    }

    @Override
    public String toString() {
        return "Listing{id=" + id + ", title='" + title + "', status=" + status + "}";
    }
}
