package com.bank;

import com.bank.app.JavalinApplication;

public class Main {
  private static final int APP_PORT = 3137;

  public static void main(String[] args) {
    new JavalinApplication(config -> {
      config.showJavalinBanner = false;
      config.enableDevLogging();
    }, APP_PORT);
  }
}
