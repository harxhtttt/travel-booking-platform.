package com.travel.exception;

/** Shows Custom Exceptions and Inheritance (base class). */
public class TravelException extends Exception {
    public TravelException(String message) { 
        super(message); 
    }
    public TravelException(String message, Throwable cause) { 
        super(message, cause); 
    }
}
