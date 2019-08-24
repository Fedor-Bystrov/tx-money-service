package com.bank.app;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
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
  private final ApplicationContext ctx;
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
      ctx = new ApplicationContext(connection);

      application = Javalin.create(config).start(appPort)
        .get("/account/list", ctx.getAccountResource()::getAccountList);
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
