package com.bank;

import com.bank.repository.AppRepository;
import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private static final String H2_URL = "jdbc:h2:mem:";

    public static void main(String[] args) {
        LOGGER.info("Initializing embedded database");
        try (final var connection = DriverManager.getConnection(H2_URL)) {
            LOGGER.info("Embedded database initialized");
            // TODO add initial accounts and transactions
            final var repo = new AppRepository(connection);
            repo.test();
            Javalin.create(JavalinConfig::enableDevLogging)
                    .start(7000)
                    .get("/", ctx -> ctx.result("Hello World"));

            // TODO
            //  - uri for account creation
            //  - uri for transaction creation
            //  - unit tests and integration tests
        } catch (SQLException ex) {
            LOGGER.error("Embedded database initialization failure", ex);
        }
    }
}
