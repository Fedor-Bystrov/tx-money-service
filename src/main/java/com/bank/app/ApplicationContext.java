package com.bank.app;

import com.bank.repository.Repository;
import com.bank.resource.AccountResource;
import com.bank.resource.TransactionResource;
import com.bank.service.AccountService;
import com.bank.service.TransactionService;

import java.sql.Connection;

class ApplicationContext {

  private final Repository repository;
  private final AccountService accountService;
  private final TransactionService transactionService;
  private final AccountResource accountResource;
  private final TransactionResource transactionResource;

  public ApplicationContext(Connection connection) {
    this.repository = new Repository(connection);
    this.accountService = new AccountService(repository);
    this.transactionService = new TransactionService(repository);
    this.accountResource = new AccountResource(accountService);
    this.transactionResource = new TransactionResource(transactionService);
  }

  public AccountResource getAccountResource() {
    return accountResource;
  }

  public TransactionResource getTransactionResource() {
    return transactionResource;
  }
}
