package com.bank;

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
//        final var app = Javalin.create(JavalinConfig::enableDevLogging).start(7000);
//        app.get("/", ctx -> ctx.result("Hello World"));
        LOGGER.info("Initializing embedded database");
        try (final var connection = DriverManager.getConnection(H2_URL);
             final var stm = connection.createStatement();
             final var rs = stm.executeQuery("SELECT 1+1")) {

            if (rs.next()) {
                System.out.println(rs.getInt(1));
            }

        } catch (SQLException ex) {
            LOGGER.error("Embedded database initialization failure", ex);
        }
    }
}
