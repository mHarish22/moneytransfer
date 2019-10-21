package com.moneytransfer.business.Impl;

import com.moneytransfer.business.IMoneyTransferService;
import com.moneytransfer.dao.Exceptions.InsufficientBalanceException;
import com.moneytransfer.dao.Exceptions.InvalidAccountException;
import com.moneytransfer.business.IAccountService;
import com.moneytransfer.dao.datamodel.Account;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/***
 * This class creates REST API for handling money transfer between 2 account.
 */
@Path("transact")
public class MoneyTransferServiceImpl implements IMoneyTransferService {

    IAccountService savingsAccount = new SavingsAccountServiceImpl();

    /***
     * Transfer money from FromAcc to toAcc.
     * Lock the reentrant locks available in the Account Objects before transferring amounts.
     * Maintain order of locks based on the account number
     * Release locks in the opposite order of acquiring.
     * In case of failure to deposit, revert the withdrawal from the source account.
     *
     * @param fromAccNo
     * @param toAccNo
     * @param amount
     * @return
     * @throws InvalidAccountException
     * @throws InsufficientBalanceException
     */
    @POST
    @Path("transfer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Account moneyTransfer(@FormParam("from") long fromAccNo, @FormParam("to") long toAccNo, @FormParam("amnt") double amount) throws InvalidAccountException, InsufficientBalanceException {
        Account fromAcc;
        Account toAcc;
        fromAcc = savingsAccount.getAccount(fromAccNo);
        toAcc = savingsAccount.getAccount(toAccNo);

        try {
            lockAccounts(fromAcc, toAcc, fromAccNo > toAccNo);
            savingsAccount.withDraw(fromAccNo, amount);
            try {
                savingsAccount.deposit(toAccNo, amount);
            } catch (Exception e) {
                savingsAccount.deposit(fromAccNo, amount);
            }
        } finally {
            unlockAccount(fromAcc, toAcc, toAccNo > fromAccNo);
        }
        return fromAcc;
    }

    /***
     * Lock accounts in order based on the fromfirst parameter
     * @param from
     * @param to
     * @param fromFirst
     */
    private void lockAccounts(Account from, Account to, boolean fromFirst) {
        if (fromFirst) {
            from.getAccountLock().lock();
            to.getAccountLock().lock();
        } else {
            to.getAccountLock().lock();
            from.getAccountLock().lock();
        }
    }

    /***
     * unlock accounts based on order determined by fromFirst
     * @param from
     * @param to
     * @param fromFirst
     */
    private void unlockAccount(Account from, Account to, boolean fromFirst) {
        if (fromFirst) {
            from.getAccountLock().unlock();
            to.getAccountLock().unlock();
        } else {
            to.getAccountLock().unlock();
            from.getAccountLock().unlock();
        }
    }

}
