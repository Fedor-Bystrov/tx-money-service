package com.bank;

import com.bank.app.JavalinApplication;
import com.bank.pojo.AccountListDto;
import com.bank.pojo.TransactionDto;
import io.restassured.RestAssured;
import org.eclipse.jetty.server.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
  private static final List<TransactionDto> INITIAL_TRANSACTIONS = getInitialTransactions();
  private static final Map<String, String> INITIAL_ACCOUNTS = getInitialAccounts();
  private static final int TEST_APP_PORT = 3138;

  private JavalinApplication javalinApp;

  @BeforeEach
  void setUp() {
    javalinApp = new JavalinApplication(config -> config.showJavalinBanner = false, TEST_APP_PORT);
    RestAssured.port = TEST_APP_PORT;
  }

  @AfterEach
  void tearDown() throws SQLException {
    javalinApp.stop();
  }

  @Test
  void integrationTest() {
    // 1. Check that accounts were initialized
    final var accountList = get("/account/list").then()
      .statusCode(Response.SC_OK)
      .contentType("application/json")
      .extract().body().jsonPath()
      .getList("", AccountListDto.class);

    assertEquals(6, accountList.size());
    assertThat(accountList, containsInAnyOrder(IntStream.range(1, 7).mapToObj(AccountListDto::new).toArray()));

    // 2. Check that transaction were initialized
    INITIAL_TRANSACTIONS.forEach((transaction) -> {
        final var txResponse = get(String.format("/transaction/%s", transaction.getTransactionId())).then()
          .statusCode(Response.SC_OK)
          .contentType("application/json")
          .extract().body().as(TransactionDto.class);
        assertEquals(transaction, txResponse);
      }
    );

    // 3. Check initial balances
    INITIAL_ACCOUNTS.forEach((accountId, accountBalance) ->
      get(String.format("/account/%s", accountId)).then()
        .statusCode(Response.SC_OK)
        .contentType("application/json")
        .body(
          "accountId", equalTo(accountBalance),
          "balance", equalTo(accountBalance)
        ));

    // 4. Check searching transactions by sender works
    get(String.format("/transaction/sender/%s", "4")).then()
      .statusCode(Response.SC_OK)
      .contentType("application/json")
      .body(
        "transactionId", equalTo("5"),
        "creationTime", equalTo("2019-08-13 04:04:00"),
        "amount", equalTo("25000.25"),
        "recipient", equalTo("5"),
        "sender", equalTo("4")
      );

    // zero transactions for sender = 6
    get(String.format("/transaction/sender/%s", "6")).then()
      .statusCode(Response.SC_OK)
      .contentType("application/json")
      .body(empty());

    // sender = 16 doesn't exist
    get(String.format("/transaction/sender/%s", "16")).then()
      .statusCode(400)
      .body(empty());

    // 5. Check searching transactions by recipient works
    get(String.format("/transaction/recipient/%s", "5")).then()
      .statusCode(Response.SC_OK)
      .contentType("application/json")
      .body(
        "transactionId", equalTo("5"),
        "creationTime", equalTo("2019-08-13 04:04:00"),
        "amount", equalTo("25000.25"),
        "recipient", equalTo("5"),
        "sender", equalTo("4")
      );

    // zero transactions for recipient = 6
    get(String.format("/transaction/recipient/%s", "6")).then()
      .statusCode(Response.SC_OK)
      .contentType("application/json")
      .body(empty());

    // recipient = 16 doesn't exist
    get(String.format("/transaction/recipient/%s", "16")).then()
      .statusCode(Response.SC_BAD_REQUEST)
      .body(empty());

    // 6. Check account creation resource works
    // 7. Check creation of transaction, check that balances change
  }

  private static List<TransactionDto> getInitialTransactions() {
    return List.of(
      new TransactionDto(1,
        LocalDateTime.parse("2019-08-13T00:00:00"),
        new BigDecimal("2500000.00"),
        1,
        1),
      new TransactionDto(2,
        LocalDateTime.parse("2019-08-13T01:01:00"),
        new BigDecimal("500000.00"),
        1,
        2),
      new TransactionDto(3,
        LocalDateTime.parse("2019-08-13T02:02:00"),
        new BigDecimal("500000.00"),
        1,
        3),
      new TransactionDto(4,
        LocalDateTime.parse("2019-08-13T03:03:00"),
        new BigDecimal("500000.00"),
        1,
        4),
      new TransactionDto(5,
        LocalDateTime.parse("2019-08-13T04:04:00"),
        new BigDecimal("25000.25"),
        4,
        5)
    );
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
