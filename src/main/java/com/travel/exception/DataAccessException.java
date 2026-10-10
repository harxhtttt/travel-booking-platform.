package com.travel.exception;

/** Shows Custom Exceptions (unchecked/RuntimeException for DAO layer). */
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message) { 
        super(message); 
    }
    public DataAccessException(String message, Throwable cause) { 
        super(message, cause); 
    }
}
