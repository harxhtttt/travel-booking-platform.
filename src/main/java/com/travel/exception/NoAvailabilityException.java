package com.travel.exception;

/** Shows Custom Exceptions (checked exception). */
public class NoAvailabilityException extends TravelException {
    public NoAvailabilityException(String message) { 
        super(message); 
    }
}
