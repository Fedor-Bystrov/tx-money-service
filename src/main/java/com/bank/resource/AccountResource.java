package com.bank.resource;

import com.bank.service.AccountService;
import io.javalin.http.Context;

public class AccountResource {
  private final AccountService accountService;

  public AccountResource(AccountService accountService) {
    this.accountService = accountService;
  }

  /**
   * Resource method for fetching account by id.
   * <strong>id is mandatory and must be > 0</strong>
   *
   * @param context javalin request context
   */
  public void getAccount(Context context) {
    int accountId = context.pathParam("accountId", Integer.class).check(id -> id > 0).get();
    context.json(accountService.getAccountById(accountId));
  }
}
