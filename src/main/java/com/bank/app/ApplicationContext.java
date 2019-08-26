package com.bank.app;

import com.bank.repository.Repository;
import com.bank.resource.AccountResource;
import com.bank.resource.TransactionResource;
import com.bank.service.AccountService;
import com.bank.service.TransactionService;

import java.sql.Connection;

class ApplicationContext {

  private final AccountResource accountResource;
  private final TransactionResource transactionResource;

  ApplicationContext(Connection connection) {
    final var repository = new Repository(connection);
    final var accountService = new AccountService(repository);
    final var transactionService = new TransactionService(repository);
    this.accountResource = new AccountResource(accountService);
    this.transactionResource = new TransactionResource(transactionService);
  }

  AccountResource getAccountResource() {
    return accountResource;
  }

  TransactionResource getTransactionResource() {
    return transactionResource;
  }
}
