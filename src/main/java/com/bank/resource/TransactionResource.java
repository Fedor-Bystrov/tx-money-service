package com.bank.resource;

import com.bank.service.TransactionService;
import io.javalin.http.Context;

public class TransactionResource {
  private final TransactionService transactionService;

  public TransactionResource(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  public void getTransactionById(Context context) {
    // TODO
  }

  public void createTransaction(Context context) {
    // TODO
  }
}
