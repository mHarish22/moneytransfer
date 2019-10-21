import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import com.google.common.util.concurrent.AtomicDouble;
import com.moneytransfer.business.IAccountService;
import com.moneytransfer.business.IMoneyTransferService;
import com.moneytransfer.business.Impl.MoneyTransferServiceImpl;
import com.moneytransfer.business.Impl.SavingsAccountServiceImpl;
import com.moneytransfer.dao.Exceptions.InsufficientBalanceException;
import com.moneytransfer.dao.Exceptions.InvalidAccountException;
import com.moneytransfer.dao.datamodel.Account;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/***
 * This class tests the concurrency for Money transfer functionality.
 * 5 thread are created which transfer money from acc1 -> acc2 and vice versa
 * The balance is checked at the end .
 */
@RunWith(ConcurrentTestRunner.class)
public class ConcurrencyTransferTest {

    static IAccountService service = new SavingsAccountServiceImpl();
    static IMoneyTransferService transferService = new MoneyTransferServiceImpl();
    static Account fromAcc;
    static Account toAcc;
    private final static int THREAD_COUNT = 5;
    private static double expectedBalance = 75.0d;

    @BeforeClass
    public static void testAccountCreate() throws InvalidAccountException {
        fromAcc = service.createAccount("Mayuri","Harish", "London");
        Assert.assertNotNull(fromAcc);
        Assert.assertEquals(fromAcc.getBalance().get(),0.0,0);
        service.deposit(fromAcc.getAccountNum(), 100d);
        toAcc = service.createAccount("Harish","M", "London");
        Assert.assertNotNull(toAcc);
        service.deposit(toAcc.getAccountNum(), 50d);


    }

    @Test
    @ThreadCount(THREAD_COUNT)
    public void testFromToTo() throws InvalidAccountException, InsufficientBalanceException {
        System.out.println("1 to 2");
        fromAcc = transferService.moneyTransfer(fromAcc.getAccountNum(), toAcc.getAccountNum(), 10d);

        Assert.assertNotNull(fromAcc);
    }

    @Test
    @ThreadCount(THREAD_COUNT)
    public void testTotoFrom() throws InvalidAccountException, InsufficientBalanceException {
        System.out.println("2 to 1");
        toAcc = transferService.moneyTransfer(toAcc.getAccountNum(), fromAcc.getAccountNum(), 5d);

        Assert.assertNotNull(toAcc);
    }

    @AfterClass
    public static void testBalance() {
        System.out.println("fromacc " + fromAcc.getBalance().get());
        System.out.println("toacc " + toAcc.getBalance().get());
        Assert.assertEquals(expectedBalance, fromAcc.getBalance().get(),0);
    }

}
