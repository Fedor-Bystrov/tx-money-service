package com.bank.resource;

import com.bank.service.AccountService;
import io.javalin.http.Context;

public class AccountResource {
  private final AccountService accountService;

  public AccountResource(AccountService accountService) {
    this.accountService = accountService;
  }

  public void getAllAccounts(Context context) {
    final var accountList = accountService.getAllAccounts();
    context.json(accountList);
  }
}
