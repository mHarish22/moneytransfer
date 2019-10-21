# Money transfer exercise

A RESTful API to transfer money between accounts.  
The accounts are all stored in an in-memory.

The API is written in Java 8 and uses jetty embedded REST server.

## Libraries used
 - **Jetty along with jersey container** is used create an embedded REST server.
 - **Jackson** for JSON serialization and deserialization.
 - **JUnit 4** - Unit testing
 - **RestAssured**  libraries used for REST API testing
  - **vmlens** libraries used for concurrency unit testing 

## Features
- Create an account.
- Deposit to or withdraw money from an account.
- Transfer money between accounts.
- In-memory database to store all accounts. (ConcurrentHashMap)
- Thread-safe
- Error capturing and returning appropriate error messages
- Unit tests for Account services, concurrency tests for money transfer and account services
- end to end testing of the REST API, start server, make API calls and test the response.

## Starting the application
1. Clone the repo and run the following command: `mvn clean package`
2. This will create a single fat jar which can be executed with `java -jar moneytransfer-1.0-SNAPSHOT-jar-with-dependencies.jar `

## Using the REST API
I am using form parameters for POST calls, this could be replaced by JSON using model objects for better design.
### Accounts
#### Creating an account  
To create an account it's a simple GET request:
```
`curl -d "fname=mayuri&sname=harish&addr=ilford" POST http://localhost:8085/account/create

``` 
The server will respond with information about the created account in a JSON format:
```
{
    "accountNum": 10000003,
    "balance": 0.0,
    "firstName": "mayuri",
    "lastName": "harish",
    "address": "ilford"
}
```

#### Get an account
To get information on an account you can submit a GET request with the account Id in as a parameter in the URL: `curl -XGET http://localhost:8085/account/detail?accNo=10000003`.
The server will respond with information about the account, which in this case will be same as above.

#### Deposit money
You can deposit money into an account by making a POST request providing the account Id and the amount you wish to deposit:
```
curl -XPOST http://localhost:8085/account/deposit -d "accno=10000003&amnt=180"
}'
```
The response will be the account details:
```
{
    "accountNum": 10000003,
    "balance": 180.0,
    "firstName": "mayuri",
    "lastName": "harish",
    "address": "ilford"
}
```

#### Withdraw money
Similarly, you can withdraw money from an account by making a POST request providing the account Id and the amount you wish to withdraw:
```
curl -XPOST http://localhost:8085/account/withdraw -d "accno=10000003&amnt=80"
```
The response will be the account details:
```
{
    "accountNum": 10000003,
    "balance": 100.0,
    "firstName": "mayuri",
    "lastName": "harish",
    "address": "ilford"
}
```

#### Transfer money
To transfer money make a POST request providing the source account Id, destination account Id and the amount to transfer:
```
curl -XPOST http://localhost:8085/transact/transfer -d '"from=10000003&to=10000001&amnt=80"

```
The response will be the from Account details

### Future possible improvements
- use JSON for post request
- Maintain transactions for an account
- API to list all transaction for an account
- API to list all accounts
- Use H2 for in-memory DB instead
- Implement logging


