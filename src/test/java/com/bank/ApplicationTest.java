package com.bank;

import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import static com.bank.Application.H2_URL;
import static com.bank.Application.newJavalinApp;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class ApplicationTest {
  private static final Map<String, Map<String, String>> INITIAL_TRANSACTIONS = getInitialTransactions();
  private static final Map<String, String> INITIAL_ACCOUNTS = getInitialAccounts();
  private static final int TEST_APP_PORT = 3137;

  private Javalin javalinApp;
  private Connection connection;

  @BeforeEach
  void setUp() throws SQLException {
    connection = DriverManager.getConnection(H2_URL);
    javalinApp = newJavalinApp(config -> config.showJavalinBanner = false, connection, TEST_APP_PORT);
    RestAssured.port = TEST_APP_PORT;
  }

  @AfterEach
  void tearDown() throws SQLException {
    connection.close();
    javalinApp.stop();
  }

  @Test
  void integrationTest() {
    // 1. Check that accounts were initialized
    get("/account/all").then()
      .statusCode(200)
      .body("accountId", hasItems(INITIAL_ACCOUNTS.keySet()));

    // 2. Check that transaction were initialized
    INITIAL_TRANSACTIONS.forEach((transactionId, transactionData) ->
      get(String.format("/transaction/%s", transactionId)).then()
        .statusCode(200)
        .body(
          "transactionId", equalTo(transactionId),
          "creationTime", equalTo(transactionData.get("creationTime")),
          "amount", equalTo(transactionData.get("amount")),
          "recipient", equalTo(transactionData.get("recipient")),
          "sender", equalTo(transactionData.get("sender"))
        )
    );

    // 3. Check initial balances
    INITIAL_ACCOUNTS.forEach((accountId, accountBalance) ->
      get(String.format("/account/%s", accountId)).then()
        .statusCode(200)
        .body(
          "accountId", equalTo(accountBalance),
          "balance", equalTo(accountBalance)
        ));

    //
    // TODO Test plan:
    //  4. Test getting transactions from accountId
    //  5. Test getting transactions to accountId
    //  6. Test creation of account
    //  7. Test creation of transaction, account1.balance and account2.balance should change
    //
  }

  private static Map<String, Map<String, String>> getInitialTransactions() {
    return Map.of(
      "1", Map.of(
        "creationTime", "2019-08-13 00:00:00",
        "amount", "2500000.00",
        "recipient", "1",
        "sender", "2"
      ),
      "2", Map.of(
        "creationTime", "2019-08-13 01:01:00",
        "amount", "500000.00",
        "recipient", "2",
        "sender", "1"
      ),
      "3", Map.of(
        "creationTime", "2019-08-13 02:02:00",
        "amount", "500000.00",
        "recipient", "3",
        "sender", "1"
      ),
      "4", Map.of(
        "creationTime", "2019-08-13 03:03:00",
        "amount", "500000.00",
        "recipient", "4",
        "sender", "1"
      ),
      "5", Map.of(
        "creationTime", "2019-08-13 04:04:00",
        "amount", "525000.25",
        "recipient", "5",
        "sender", "4"
      ));
  }

  private static Map<String, String> getInitialAccounts() {
    return Map.of(
      "1", "1000000.00",
      "2", "500000.00",
      "3", "500000.00",
      "4", "474999.75",
      "5", "25000.25",
      "6", "0.00"
    );
  }
}
