package com.bank;

import io.javalin.Javalin;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.bank.Application.newJavalinApp;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

class ApplicationTest {
  private static final int TEST_APP_PORT = 3137;
  private static final String H2_TEST_URL = "jdbc:h2:mem:test";

  private static final int HTTP_STATUS_OK = 200;

  private Javalin javalinApp;
  private Connection connection;

  @BeforeEach
  void setUp() throws SQLException {
    connection = DriverManager.getConnection(H2_TEST_URL);
    javalinApp = newJavalinApp(javalinConfig -> javalinConfig.showJavalinBanner = false, connection, TEST_APP_PORT);
    RestAssured.port = TEST_APP_PORT;
  }

  @AfterEach
  void tearDown() throws SQLException {
    connection.close();
    javalinApp.stop();
  }

  @Test
  void name() {
    get("/").then()
      .statusCode(HTTP_STATUS_OK)
      .body("test", equalTo("HelloWorld"));
  }
}
