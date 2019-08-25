package com.bank.resource;

import com.bank.service.TransactionService;
import io.javalin.http.Context;

public class TransactionResource {
  private final TransactionService transactionService;

  public TransactionResource(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  /**
   * Resource method for fetching transaction by id.
   * <strong>id is mandatory and must be > 0</strong>
   *
   * @param context javalin request context
   */
  public void getTransactionById(Context context) {
    int transactionId = context.pathParam("transactionId", Integer.class).check(id -> id > 0).get();
    context.json(transactionService.getTransactionById(transactionId));
  }

  public void createTransaction(Context context) {
    // TODO
  }
}
