package com.bank;

import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.bank.Application.newJavalinApp;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class ApplicationTest {
  private static final Map<String, Map<String, String>> INITIAL_TRANSACTIONS = getInitialTransactions();
  private static final Map<String, String> INITIAL_ACCOUNTS = getInitialAccounts();
  private static final int TEST_APP_PORT = 3137;

  private Javalin javalinApp;

  @BeforeEach
  void setUp() {
    javalinApp = newJavalinApp(config -> config.showJavalinBanner = false, TEST_APP_PORT);
    RestAssured.port = TEST_APP_PORT;
  }

  @AfterEach
  void tearDown() {
    javalinApp.stop();
  }

  @Test
  void integrationTest() {
    // 1. Check that accounts were initialized
    get("/account/list").then()
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

    // 4. Check searching transactions by sender works
    get(String.format("/transaction/sender/%s", "4")).then()
      .statusCode(200)
      .body(
        "transactionId", equalTo("5"),
        "creationTime", equalTo("2019-08-13 04:04:00"),
        "amount", equalTo("25000.25"),
        "recipient", equalTo("5"),
        "sender", equalTo("4")
      );

    // zero transactions for sender = 6
    get(String.format("/transaction/sender/%s", "6")).then()
      .statusCode(200)
      .body(empty());

    // sender = 16 doesn't exist
    get(String.format("/transaction/sender/%s", "16")).then()
      .statusCode(400)
      .body(empty());

    // 5. Check searching transactions by recipient works
    get(String.format("/transaction/recipient/%s", "5")).then()
      .statusCode(200)
      .body(
        "transactionId", equalTo("5"),
        "creationTime", equalTo("2019-08-13 04:04:00"),
        "amount", equalTo("25000.25"),
        "recipient", equalTo("5"),
        "sender", equalTo("4")
      );

    // zero transactions for recipient = 6
    get(String.format("/transaction/recipient/%s", "6")).then()
      .statusCode(200)
      .body(empty());

    // recipient = 16 doesn't exist
    get(String.format("/transaction/recipient/%s", "16")).then()
      .statusCode(400)
      .body(empty());

    // 6. Check account creation resource works
    // 7. Check creation of transaction, check that balances change
  }

  private static Map<String, Map<String, String>> getInitialTransactions() {
    return Map.of(
      "1", Map.of(
        "creationTime", "2019-08-13 00:00:00",
        "amount", "2500000.00",
        "recipient", "1",
        "sender", "1"
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
        "amount", "25000.25",
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
