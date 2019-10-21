import com.moneytransfer.MoneyTransferApp;
import com.moneytransfer.dao.datamodel.Account;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import org.eclipse.jetty.server.Server;
import org.junit.*;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestMoneyTransferAPI {
//curl -d "param1=value1&param2=value2" -X POST http://localhost:3000/data

    static long accFrom;
    static long accNoTo;
    static Server jettyServer;

    @BeforeClass
    public static void init() {
        try {
            jettyServer = MoneyTransferApp.startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8085;
    }

    @Test
    public void testAccountCreate() throws Exception {
        Account acc = RestAssured.given().formParam("fname","harish").formParam("sname","mukund").formParam("addr","ilford")
                .when().post("/account/create").then().extract()
                .as(Account.class);
        Assert.assertNotNull(acc);
        accFrom = acc.getAccountNum();
        Assert.assertEquals("harish",acc.getFirstName());

        Account accTo = RestAssured.given().formParam("fname","Mayuri").formParam("sname","Harish").formParam("addr","ilford")
                .when().post("/account/create").then().extract()
                .as(Account.class);
        Assert.assertNotNull(accTo);
        accNoTo = accTo.getAccountNum();

    }


    @Test
    public void testAccountDeposit() throws Exception {
        double balance = 100.0d;
        Account acc = RestAssured.with().formParam("accno", accFrom).formParam("amnt", "100").when().post("/account/deposit").then().extract().as(Account.class);
        Assert.assertEquals(balance,acc.getBalance().get(),0);
    }

    @Test
    public void testAccountWithdraw() throws Exception {
        double balance = 90.0d;
        Account acc = RestAssured.with().formParam("accno", accFrom).formParam("amnt", "10").when().post("/account/withdraw").then().extract().as(Account.class);
        Assert.assertEquals(balance,acc.getBalance().get(),0);
    }

    @Test
    public void testMoneyTransfer() throws Exception {
        Account accFrom = RestAssured.with().formParam("from", TestMoneyTransferAPI.accFrom).formParam("to",accNoTo)
                .formParam("amnt","20").when().post("/transact/transfer").then().extract().as(Account.class);
        double balance = 70.0d;
        Assert.assertEquals( balance,accFrom.getBalance().get(),0);
    }


    @Test
    public void testTransferedAccountDetail() {
        double balance = 20.0d;
        Account acc = RestAssured.with().queryParam("accNo", accNoTo).when().get("/account/detail").then().extract().as(Account.class);
        Assert.assertEquals(acc.getBalance().get(),balance,0);
    }

    @AfterClass
    public static void shutDownServer() {
        try {
            jettyServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jettyServer.destroy();
        }

    }

} 
