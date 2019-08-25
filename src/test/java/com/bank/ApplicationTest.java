package com.bank;

import com.bank.app.JavalinApplication;
import com.bank.pojo.AccountDto;
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

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {
  private static final List<TransactionDto> INITIAL_TRANSACTIONS = getInitialTransactions();
  private static final List<AccountDto> INITIAL_ACCOUNTS = getInitialAccounts();
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
    // 1. Check that transaction were initialized
    INITIAL_TRANSACTIONS.forEach((transaction) -> {
        var txResponse = get(String.format("/transaction/%s", transaction.getTransactionId())).then()
          .statusCode(Response.SC_OK)
          .contentType("application/json")
          .extract().body().as(TransactionDto.class);
        assertEquals(transaction, txResponse);
      }
    );

    get(String.format("/transaction/%d", 999)).then()
      .statusCode(Response.SC_BAD_REQUEST)
      .contentType("application/json")
      .body("error", equalTo("Invalid transaction id"));

    // TODO check BadRequestResponse of id not string and id = 999

    // 2. Check initial accounts
    INITIAL_ACCOUNTS.forEach((account) -> {
      var accountResponse = get(String.format("/account/%s", account.getAccountId())).then()
        .statusCode(Response.SC_OK)
        .contentType("application/json")
        .extract().body().as(AccountDto.class);
      assertEquals(account, accountResponse);
    });

    // TODO check BadRequestResponse of id not string and id = 999

    // 3. Check creation of transaction, check that balances change
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

  private static List<AccountDto> getInitialAccounts() {
    return List.of(
      new AccountDto(1, "1000000.00"),
      new AccountDto(2, "500000.00"),
      new AccountDto(3, "500000.00"),
      new AccountDto(4, "474999.75"),
      new AccountDto(5, "25000.25"),
      new AccountDto(6, "0.00")
    );
  }
}
