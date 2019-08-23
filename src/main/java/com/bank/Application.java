package com.bank;

import com.bank.repository.AppRepository;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

public class Application {
  private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
  private static final String H2_URL = "jdbc:h2:mem:";
  private static final int APP_PORT = 3137;

  public static void main(String[] args) {
    LOGGER.info("Initializing embedded database");
    try (final var connection = DriverManager.getConnection(H2_URL)) {
      LOGGER.info("Embedded database initialized");
      // TODO add initial accounts and transactions
      newJavalinApp(javalinConfig -> {
        javalinConfig.showJavalinBanner = false;
        javalinConfig.enableDevLogging();
      }, connection, APP_PORT);

      // TODO
      //  - uri for account creation
      //  - uri for transaction creation
      //  - unit tests and integration tests
    } catch (SQLException ex) {
      LOGGER.error("Embedded database initialization failure", ex);
    }
  }

  static Javalin newJavalinApp(Consumer<JavalinConfig> config, Connection connection, int appPort) throws SQLException {
    final var javalinApp = Javalin.create(config).start(appPort);
    final var repository = new AppRepository(connection);
    repository.test();

    javalinApp
      .get("/", ctx -> ctx.result("Hello World"));

    return javalinApp;
  }
}
