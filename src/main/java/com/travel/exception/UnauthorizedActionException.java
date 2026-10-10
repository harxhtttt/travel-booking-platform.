package com.travel.exception;

/** Shows Custom Exceptions (checked exception). */
public class UnauthorizedActionException extends TravelException {
    public UnauthorizedActionException(String message) { 
        super(message); 
    }
}
