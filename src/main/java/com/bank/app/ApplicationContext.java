package com.bank.app;

import com.bank.repository.AppRepository;
import com.bank.resource.AccountResource;
import com.bank.service.AccountService;

import java.sql.Connection;

class ApplicationContext {

  private final AppRepository appRepository;
  private final AccountService accountService;
  private final AccountResource accountResource;

  public ApplicationContext(Connection connection) {
    this.appRepository = new AppRepository(connection);
    this.accountService = new AccountService(appRepository);
    this.accountResource = new AccountResource(accountService);
  }

  public AccountResource getAccountResource() {
    return accountResource;
  }
}
