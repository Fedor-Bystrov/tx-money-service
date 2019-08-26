package com.bank.resource;

import com.bank.exception.DatabaseException;
import com.bank.exception.EntityNotFoundException;
import com.bank.service.AccountService;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountResource.class);

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
    final int accountId = context.pathParam("accountId", Integer.class)
      .check(id -> id > 0)
      .get();

    try {
      context.json(accountService.getAccountById(accountId));
    } catch (EntityNotFoundException ex) {
      LOGGER.info("No account with given id; accountId={}", accountId);
      throw new BadRequestResponse("Invalid account id");
    } catch (DatabaseException ex) {
      LOGGER.error("Database exception during getAccount query", ex);
      throw new InternalServerErrorResponse();
    }
  }
}
