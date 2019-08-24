package com.bank.app;

import com.bank.repository.Repository;
import com.bank.resource.AccountResource;
import com.bank.service.AccountService;

import java.sql.Connection;

class ApplicationContext {

  private final Repository repository;
  private final AccountService accountService;
  private final AccountResource accountResource;

  public ApplicationContext(Connection connection) {
    this.repository = new Repository(connection);
    this.accountService = new AccountService(repository);
    this.accountResource = new AccountResource(accountService);
  }

  public AccountResource getAccountResource() {
    return accountResource;
  }
}
