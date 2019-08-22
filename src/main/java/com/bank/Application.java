package com.bank;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;

public class Application {
    public static void main(String[] args) {
        final var app = Javalin.create(JavalinConfig::enableDevLogging).start(7000);
        app.get("/", ctx -> ctx.result("Hello World"));
    }
}
