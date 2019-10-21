package com.moneytransfer.dao;

import com.moneytransfer.dao.Exceptions.InvalidAccountException;
import com.moneytransfer.dao.datamodel.Account;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/***
 * This class is used as an in-memory DB.
 * Using ConcurrentHashmap instead of an in-memory DB like H2.
 * Using ConcurrentHashmap to keep it thread safe
 */
public class Data {

    static AtomicLong accountNumber = new AtomicLong(10000000);
    static ConcurrentHashMap<Long, Account> accounts = new ConcurrentHashMap();

    public static Account getAccount(long accNo) throws InvalidAccountException {
        Account account = accounts.get(accNo);
        if(account != null)
        return account;
        else throw new InvalidAccountException("Account number " + accNo + " does not exist");
    }

    /***
     * Perform atomic operation for adding an account to the map.
     * @param acc
     * @return
     */
    public static synchronized Account addAccount(Account acc) {
        return accounts.putIfAbsent(acc.getAccountNum(), acc);
    }

    /***
     * Perform atomic operation so as to maintain unique account number
     * @return
     */
    public static long getNextAccountNumber() {
        return accountNumber.addAndGet(1);
    }
}
