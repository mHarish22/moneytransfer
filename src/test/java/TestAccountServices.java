import com.moneytransfer.business.IAccountService;
import com.moneytransfer.business.IMoneyTransferService;
import com.moneytransfer.business.Impl.MoneyTransferServiceImpl;
import com.moneytransfer.business.Impl.SavingsAccountServiceImpl;
import com.moneytransfer.dao.Exceptions.InsufficientBalanceException;
import com.moneytransfer.dao.Exceptions.InvalidAccountException;
import com.moneytransfer.dao.datamodel.Account;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/***
 * This test suite tests the service methods for account and money transfer
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAccountServices {

    static IAccountService service = new SavingsAccountServiceImpl();
    static IMoneyTransferService transferService = new MoneyTransferServiceImpl();
    static Account fromAcc;
    static Account toAcc;

    /***
     * Test Account creation
     */
    @Test
    public void testAccountCreate()  {
        fromAcc = service.createAccount("Mayuri","Harish", "London");
        Assert.assertNotNull(fromAcc);
        Assert.assertEquals(fromAcc.getBalance().get(),0.0,0);
        toAcc = service.createAccount("Harish","M", "London");
    }

    /***
     * Test account deposit and assert balance.
     * @throws InvalidAccountException
     */
    @Test
    public void testAccountDeposit() throws InvalidAccountException {
        fromAcc = service.deposit(fromAcc.getAccountNum(), 100d);
        Assert.assertNotNull(fromAcc);
        Assert.assertEquals(fromAcc.getBalance().get(),100.0,0);
    }

    /***
     * Test for exception handling in case of invalid account number
     * @throws InvalidAccountException
     */
    @Test(expected=InvalidAccountException.class)
    public void testAccountDepositError() throws InvalidAccountException {
        fromAcc = service.deposit(1l, 100d);
        Assert.assertNotNull(fromAcc);
        Assert.assertEquals(fromAcc.getBalance().get(),100.0,0);
    }

    /***
     * Test Account withdraw functionality, assert deposit
     * @throws InvalidAccountException
     * @throws InsufficientBalanceException
     */
    @Test
    public void testAccountWithdraw() throws InvalidAccountException, InsufficientBalanceException {
        fromAcc = service.withDraw(fromAcc.getAccountNum(), 10d);
        Assert.assertNotNull(fromAcc);
        Assert.assertEquals(fromAcc.getBalance().get(),90.0,0);
    }

    /***
     * Test for exception handling in case of insufficient balance
     *  @throws InvalidAccountException
     * @throws InsufficientBalanceException
     */
    @Test(expected = InsufficientBalanceException.class)
    public void testAccountWithdrawError() throws InvalidAccountException, InsufficientBalanceException {
        fromAcc = service.withDraw(fromAcc.getAccountNum(), 2000d);
    }

    /***
     * Test for money transfer between 2 accounts created
     * @throws InvalidAccountException
     * @throws InsufficientBalanceException
     */

    @Test
    public void testMoneyTransfer() throws InvalidAccountException, InsufficientBalanceException {
        fromAcc = transferService.moneyTransfer(fromAcc.getAccountNum(), toAcc.getAccountNum(),20d);
        Assert.assertEquals(70.0d, fromAcc.getBalance().get(),0);
    }



}
