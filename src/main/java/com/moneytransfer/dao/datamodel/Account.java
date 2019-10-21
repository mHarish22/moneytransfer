package com.moneytransfer.dao.datamodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.util.concurrent.AtomicDouble;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/***
 * Data Model for holding account details.
 */
public class Account {
    // Let this value be set only in constructor and not have any setter method, so that it cant be changed after initialization
    private long accountNum;
    private AtomicDouble balance = new AtomicDouble();
    private String firstName;
    private String lastName;
    private String address;
    @JsonIgnore
    private ReentrantLock accountLock;

    /***
     * Creates Account object using provided parameters and also creates a ReentrantLock for the object.
     * @param nextAccountNumber
     * @param balance
     * @param firstName
     * @param lastName
     * @param address
     */
    public Account(long nextAccountNumber, double balance, String firstName, String lastName, String address) {
        this.accountNum = nextAccountNumber;
        this.balance.getAndSet(balance);
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        accountLock = new ReentrantLock();
    }

    public long getAccountNum() {
        return accountNum;
    }

    public AtomicDouble getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance.getAndAdd(balance);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ReentrantLock getAccountLock() {
        return accountLock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountNum == account.accountNum &&
                balance.equals(account.balance) &&
                firstName.equals(account.firstName) &&
                lastName.equals(account.lastName) &&
                address.equals(account.address) &&
                accountLock.equals(account.accountLock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNum, balance, firstName, lastName, address, accountLock);
    }

    /***
     * Default construcor for json deserialization
     */
    public Account() {
        super();
    }
}
