# tx-money-service 

An RESTful API for money transfers between accounts. 
Provides 6 accounts, 5 example money transactions 
and an interface to fetch account balance, transaction details and create new transactions 
(sending money between those six accounts).

An executable jar with project can be downloaded from [link](https://github.com/Fedor-Bystrov/tx-money-service/raw/master/jars/tx-money-service-0.1.0.jar).

## Getting Started

### Run prebuild jar file

Executing `java -jar tx-money-service-0.1.0.jar` will start application on `http://localhost:3137`

Jar file with application located in `./jars`

### Build and run from sources

In the project root directory run:
 - `./gradlew clean build run` to build and run project
 - `./gradlew clean jar` to build executable jar archive and then `java -jar ./build/libs/tx-money-service-0.1.0.jar`

## Project structure

```
├── jars                                                        # Prebuilt executable jars 
│   └── tx-money-service-0.1.0.jar
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── bank
    │   │           ├── app
    │   │           │   ├── ApplicationContext.java             
    │   │           │   └── JavalinApplication.java             # Javalin configuration  
    │   │           ├── db
    │   │           │   └── Functions.java                      # Embedded database stored procedures
    │   │           ├── exception
    │   │           │   ├── DatabaseException.java
    │   │           │   ├── EntityNotFoundException.java
    │   │           │   └── NotEnoughMoneyException.java
    │   │           ├── Main.java                               # Application entry point
    │   │           ├── pojo
    │   │           │   ├── AccountDto.java
    │   │           │   ├── ErrorResponse.java
    │   │           │   ├── PostTransactionDto.java
    │   │           │   └── TransactionDto.java
    │   │           ├── repository
    │   │           │   └── Repository.java
    │   │           ├── resource
    │   │           │   ├── AccountResource.java
    │   │           │   └── TransactionResource.java
    │   │           └── service
    │   │               ├── AccountService.java
    │   │               └── TransactionService.java
    │   └── resources
    │       └── initdb.sql                                      # Embedded database initialization script                               
    └── test
        └── java
            └── com
                └── bank
                    ├── ApplicationTest.java                    # Integration test
                    ├── db
                    │   └── FunctionsTest.java
                    ├── repository
                    │   └── RepositoryTest.java
                    └── service
                        ├── AccountServiceTest.java
                        └── TransactionServiceTest.java

```

## API Overview

1. [Introduction](#Introduction)
2. [Getting account balance](#getting-account-balance)
3. [Getting transaction details](#getting-transaction-details)
4. [Creating new transaction](#creating-new-transaction)

### Introduction

Application comes with six accounts:

| accountId | balance (coins) |
|-----------|---------|
|1|1_000_000.00|
|2|500_000.00|
|3|500_000.00|
|4|474_999.75|
|5|25_000.25|
|6|0.00|

and five example transactions:

| transactionId | creation_time | amount | recipient | sender|
|---------------|---------------|--------|-----------|-------|
|1|2019-08-13 00:00|2500000.00|1|0|
|2|2019-08-13 01:01|500000.00|2|1|
|3|2019-08-13 02:02|500000.00|3|1|
|4|2019-08-13 03:03|500000.00|4|1|
|5|2019-08-13 04:04|25000.25|5|4|

where: 
- `sender` -  accountId that sends money
- `recipient` - accountId that receives money

The API allows you to create new transactions thereby _**sending money between those six accounts**_.

### Getting account balance

`curl -X GET http://localhost:3137/account/{accountId}`

For example, `curl -X GET http://localhost:3137/account/1` will return
```json
{
  "accountId": 1,
  "balance": 1000000.00
}
```

### Getting transaction details

`curl -X GET http://localhost:3137/transaction/{transactionId}`

For example, `curl -X GET http://localhost:3137/transaction/1 ` will return
```json
{
  "transactionId": 1,
  "creationTime": "2019-08-13T00:00:00",
  "amount": 2500000.00,
  "sender": 0,
  "recipient": 1
}
```
where: 
- `sender` -  accountId that sends money
- `recipient` - accountId that receives money

### Creating new transaction

`curl -d '{ "amount":{amount}, "recipient":{accountId}, "sender":{accountId} }' -H "Content-Type: application/json" -X POST http://localhost:3137/transaction`

returns
```json
{
  "transactionId": "{id of created transaction}"
}
```

For example: 
`curl -d '{"amount":2000, "recipient":6, "sender":5}' -H "Content-Type: application/json" -X POST http://localhost:3137/transaction`
will return 
```json
{
  "transactionId": 6
}
```

Then, we can check that transaction is created:

`http://localhost:3137/transaction/6`
```json
{
  "transactionId": 6,
  "creationTime": "2019-08-27T00:00:00",
  "amount": 2000,
  "sender": 5,
  "recipient": 6
}
```


and sender and recipient balances are updated:

`curl -X GET http://localhost:3137/account/5` - sender
```json
{
  "accountId": 5,
  "balance": 23000.25
}
```

`curl -X GET http://localhost:3137/account/6` - recipient
```json
{
  "accountId": 6,
  "balance": 2000.00
}
```
