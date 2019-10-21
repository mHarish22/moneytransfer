package com.moneytransfer.dao.Exceptions;

/***
 * Exception to be thrown when an invalid account number is being used to access account details.
 */
public class InvalidAccountException extends ApplicationException {

    public InvalidAccountException(String message) {
        super(message);

    }
}
