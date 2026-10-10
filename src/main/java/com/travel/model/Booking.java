package com.travel.model;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** Shows Encapsulation. */
public class Booking {
    private int id;
    private int travelerId;
    private int listingId;
    private BookingStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime bookedAt;

    public Booking() {}
    public Booking(int id, int travelerId, int listingId, BookingStatus status, LocalDate startDate, LocalDate endDate, LocalDateTime bookedAt) {
        this.id = id; 
        this.travelerId = travelerId; 
        this.listingId = listingId; 
        this.status = status; 
        this.startDate = startDate; 
        this.endDate = endDate; 
        this.bookedAt = bookedAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getTravelerId() { return travelerId; }
    public void setTravelerId(int travelerId) { this.travelerId = travelerId; }
    public int getListingId() { return listingId; }
    public void setListingId(int listingId) { this.listingId = listingId; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public LocalDateTime getBookedAt() { return bookedAt; }
    public void setBookedAt(LocalDateTime bookedAt) { this.bookedAt = bookedAt; }

    @Override
    public String toString() {
        return "Booking{id=" + id + ", travelerId=" + travelerId + ", listingId=" + listingId + ", status=" + status + "}";
    }
}
