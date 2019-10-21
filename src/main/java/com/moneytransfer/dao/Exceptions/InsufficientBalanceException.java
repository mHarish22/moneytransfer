package com.moneytransfer.dao.Exceptions;

/***
 * Exception to be thrown when there is insufficient balance in the account
 */
public class InsufficientBalanceException extends ApplicationException {

    public InsufficientBalanceException(String message) {
        super(message);
    }
}
