package com.moneytransfer.business;

import com.moneytransfer.dao.Exceptions.InsufficientBalanceException;
import com.moneytransfer.dao.Exceptions.InvalidAccountException;
import com.moneytransfer.dao.datamodel.Account;

/***
 * Interface to transfer money between 2 accounts
 */

public interface IMoneyTransferService {
    public Account moneyTransfer(long fromAccNo, long toAccNo, double amount) throws InvalidAccountException, InsufficientBalanceException ;
}
