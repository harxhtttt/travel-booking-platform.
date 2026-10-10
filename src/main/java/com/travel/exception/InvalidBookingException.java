package com.travel.exception;

/** Shows Custom Exceptions (checked exception). */
public class InvalidBookingException extends TravelException {
    public InvalidBookingException(String message) { 
        super(message); 
    }
}
