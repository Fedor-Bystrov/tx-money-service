package com.bank;

import com.bank.repository.AppRepository;
import com.bank.resource.AccountResource;
import com.bank.service.AccountService;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

public class Main {
  private static final String H2_URL = "jdbc:h2:mem:app;INIT=RUNSCRIPT FROM 'classpath:initdb.sql'";
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
  private static final int APP_PORT = 3137;

  public static void main(String[] args) {
    newJavalinApp(config -> {
      config.showJavalinBanner = false;
      config.enableDevLogging();
    }, APP_PORT);
  }

  static Javalin newJavalinApp(Consumer<JavalinConfig> config, int appPort) {
    try {
      LOGGER.info("Initializing embedded database");
      final var connection = DriverManager.getConnection(H2_URL);
      LOGGER.info("Embedded database initialized");

      final var javalinApp = Javalin.create(config).start(appPort);
      final var repository = new AppRepository(connection);
      final var accountService = new AccountService(repository);
      final var accountResource = new AccountResource(accountService);

      javalinApp
        .get("/account/list", accountResource::getAccountList);

      return javalinApp;
    } catch (SQLException ex) {
      LOGGER.error("Embedded database initialization failure", ex);
      throw new RuntimeException("Database initialization failure");
    }
  }
}
