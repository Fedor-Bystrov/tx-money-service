package com.bank.app;

import com.bank.pojo.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.BadRequestResponse;
import io.javalin.plugin.json.JavalinJackson;
import org.eclipse.jetty.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

public class JavalinApplication {
  private static final Logger LOGGER = LoggerFactory.getLogger(JavalinApplication.class);
  private static final String H2_URL = "jdbc:h2:mem:app;INIT=RUNSCRIPT FROM 'classpath:initdb.sql'";

  private final Connection connection;
  private final Javalin application;

  /**
   * Initializes connection to the embedded database and starts Javalin application on specified port
   *
   * @param config  javalin config
   * @param appPort application port
   */
  public JavalinApplication(Consumer<JavalinConfig> config, int appPort) {
    try {
      LOGGER.info("Initializing embedded database");
      this.connection = DriverManager.getConnection(H2_URL);

      LOGGER.info("Initializing application context");
      final var appCtx = new ApplicationContext(connection);

      // for java.time serialization
      JavalinJackson.configure(new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS));

      application = Javalin.create(config).start(appPort)
        .get("/account/:accountId", appCtx.getAccountResource()::getAccount)
        .get("/transaction/:transactionId", appCtx.getTransactionResource()::getTransactionById)
        .post("/transaction", appCtx.getTransactionResource()::createTransaction)
        .exception(BadRequestResponse.class, (e, context) -> {
          context.status(Response.SC_BAD_REQUEST);
          context.json(new ErrorResponse(e));
        });
    }
    catch (SQLException ex) {
      LOGGER.error("Embedded database initialization failure", ex);
      throw new RuntimeException("Application initialization failure");
    }
  }

  public void stop() throws SQLException {
    connection.close();
    application.stop();
  }
}
