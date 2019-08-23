package com.bank;

import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.bank.Application.H2_URL;
import static com.bank.Application.newJavalinApp;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

class ApplicationTest {
  private static final int TEST_APP_PORT = 3137;

  private Javalin javalinApp;
  private Connection connection;

  @BeforeEach
  void setUp() throws SQLException {
    connection = DriverManager.getConnection(H2_URL);
    javalinApp = newJavalinApp(javalinConfig -> javalinConfig.showJavalinBanner = false, connection, TEST_APP_PORT);
    RestAssured.port = TEST_APP_PORT;
  }

  @AfterEach
  void tearDown() throws SQLException {
    connection.close();
    javalinApp.stop();
  }

  //
  // TODO Test plan:
  //  1. Test getting account by id (check balance)
  //  2. Test getting transaction by transactionId
  //  3. Test getting transactions from accountId
  //  4. Test getting transactions to accountId
  //  5. Test creation of account
  //  6. Test creation of transaction, account1.balance and account2.balance should change
  //

  @Test
  void integrationTest() {
    get("/").then()
      .statusCode(200)
      .body("test", equalTo("HelloWorld"));
  }
}
