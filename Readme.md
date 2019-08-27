# tx-money-service 

An RESTful API for money transfers between accounts. 
Provides 6 preinitialized accounts, 5 example money transactions 
and an interface to fetch account balance, transaction details and create new transactions.

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
2. [Getting account balance](#Getting account balance)
3. [Getting transaction details](#Getting transaction details)
4. [Creating new transaction](#Creating new transaction)

### Introduction
### Getting account balance
### Getting transaction details
### Creating new transaction