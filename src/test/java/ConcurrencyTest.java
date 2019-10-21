import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.anarsoft.vmlens.concurrent.junit.ThreadCount;
import com.google.common.util.concurrent.AtomicDouble;
import com.moneytransfer.business.IAccountService;
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
 * This class tests the concurrency for account withdrawl and deposit on a particular account.
 * Creating 5 thread and at the end of the test asserting the balance.
 */
@RunWith(ConcurrentTestRunner.class)
public class ConcurrencyTest {

    static IAccountService service = new SavingsAccountServiceImpl();
    static Account fromAcc;
    private final static int THREAD_COUNT = 5;
    private static AtomicDouble expectedBalance = new AtomicDouble(450.0);

    @BeforeClass
    public static void testAccountCreate()  {
        fromAcc = service.createAccount("Mayuri","Harish", "London");
        Assert.assertNotNull(fromAcc);
        Assert.assertEquals(fromAcc.getBalance().get(),0.0,0);
    }

    @Test
    @ThreadCount(THREAD_COUNT)
    public void testAccountDeposit() throws InvalidAccountException {

        fromAcc = service.deposit(fromAcc.getAccountNum(), 100d);

        Assert.assertNotNull(fromAcc);
    }

    /***
     * In case withdraw gets called before deposit, exception will be thrown, catch the exception and make changes to balance expected.
     * @throws InvalidAccountException
     */
    @Test
    @ThreadCount(THREAD_COUNT)
    public void testAccountwithdraw() throws InvalidAccountException {
        try {
            fromAcc = service.withDraw(fromAcc.getAccountNum(), 10d);
        } catch (InsufficientBalanceException e) {
            expectedBalance.getAndAdd(10);
        }
        Assert.assertNotNull(fromAcc);
    }

    /***
     * At the end of running of all the threads, and all test cases
     */
    @AfterClass
    public static void testBalance() {
        Assert.assertEquals(expectedBalance.get(), fromAcc.getBalance().get(),0);
    }

}
