package com.moneytransfer.dao.Exceptions;

/***
 * A wrapper for all the exceptions thrown by the application
 */
public class ApplicationException extends Exception {
    public ApplicationException(String message) {
        super(message);
    }
}
