package com.moneytransfer.business.Impl;

import com.moneytransfer.dao.Data;
import com.moneytransfer.dao.Exceptions.InsufficientBalanceException;
import com.moneytransfer.dao.Exceptions.InvalidAccountException;
import com.moneytransfer.business.IAccountService;
import com.moneytransfer.dao.datamodel.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/***
 * Rest API for performing account related operations.
 */
@Path("/account")
public class SavingsAccountServiceImpl implements IAccountService {

    /***
     * Get the details for account provided.
     * @param accNo
     * @return
     * @throws InvalidAccountException
     */
    @GET
    @Path("detail")
    @Produces(MediaType.APPLICATION_JSON)
    public Account getAccount(@QueryParam("accNo") long accNo) throws InvalidAccountException {
        Account acc = Data.getAccount(accNo);
        return acc;
    }

    /***
     * Create an account with the details provided. Make sure that the account is added into the DB, else create a new object with
     * the next generated account number. This shouldnt occur as the account number incriment is handled as atomin operation.
     * Measures to handlle concurrency.
     * @param firstName
     * @param lastName
     * @param address
     * @return
     */
    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Account createAccount(@FormParam("fname") String firstName, @FormParam("sname") String lastName, @FormParam("addr") String address) {
        Account acc, accountCreated;
        do {
            acc = new Account(Data.getNextAccountNumber(), 0, firstName, lastName, address);
            accountCreated = Data.addAccount(acc);
        } while (accountCreated != null);
        return acc;
    }

    /***
     * Withdraw the amount specified from the given account.
     * using AtomicDouble for account balance to allow atomic operations and thread safety.
     * @param accNo
     * @param amount
     * @return
     * @throws InvalidAccountException
     * @throws InsufficientBalanceException
     */
    @POST
    @Path("withdraw")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Account withDraw(@FormParam("accno") long accNo, @FormParam("amnt") double amount) throws InvalidAccountException, InsufficientBalanceException {
        Account acc = Data.getAccount(accNo);
        try {
            acc.getAccountLock().lock();
            if (acc.getBalance().get() > amount) {
                acc.getBalance().addAndGet(-amount);
                return acc;
            }
            throw new InsufficientBalanceException(accNo + " account doesnt have sufficient balance.");
        } finally {
            acc.getAccountLock().unlock();
        }
    }

    /***
     * Deposit the amount specified to the given account.
     * @param accNo
     * @param amount
     * @return
     * @throws InvalidAccountException
     */
    @POST
    @Path("deposit")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Account deposit(@FormParam("accno") long accNo, @FormParam("amnt") double amount) throws InvalidAccountException {
        Account acc = Data.getAccount(accNo);
        // Lock isnt necessary as the addAndGet is an atomic operation.
        acc.getBalance().addAndGet(amount);
        return acc;
    }
}
