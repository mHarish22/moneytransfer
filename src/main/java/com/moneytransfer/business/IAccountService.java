package com.moneytransfer.business;

import com.moneytransfer.dao.Exceptions.InsufficientBalanceException;
import com.moneytransfer.dao.Exceptions.InvalidAccountException;
import com.moneytransfer.dao.datamodel.Account;

/***
 * Service interface to perform various operations on the account
 */
public interface IAccountService {

    Account getAccount(long accNo) throws InvalidAccountException;

    Account createAccount(String firstName, String LastName, String address);

    Account withDraw(long accNo, double amount) throws InvalidAccountException, InsufficientBalanceException;

    Account deposit(long accNo, double amount) throws InvalidAccountException;

}
