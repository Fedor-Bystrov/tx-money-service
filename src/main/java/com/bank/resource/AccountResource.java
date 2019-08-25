package com.bank.resource;

import com.bank.service.AccountService;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

  private final AccountService accountService;

  public AccountResource(AccountService accountService) {
    this.accountService = accountService;
  }

  public void getAccount(Context context) {
    // TODO
  }
}
