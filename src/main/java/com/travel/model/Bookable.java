package com.travel.model;
import com.travel.exception.TravelException;

/** Shows Interface Abstraction. */
public interface Bookable {
    boolean checkAvailability();
    boolean book(int travelerId) throws TravelException;
}
