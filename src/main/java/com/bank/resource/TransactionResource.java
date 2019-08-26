package com.bank.resource;

import com.bank.exception.DatabaseException;
import com.bank.exception.EntityNotFoundException;
import com.bank.exception.NotEnoughMoneyException;
import com.bank.pojo.PostTransactionDto;
import com.bank.service.TransactionService;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;
import org.eclipse.jetty.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

public class TransactionResource {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionResource.class);

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
    int transactionId = context.pathParam("transactionId", Integer.class)
      .check(id -> id > 0)
      .get();

    try {
      context.json(transactionService.getTransactionById(transactionId));
    } catch (EntityNotFoundException ex) {
      LOGGER.info("No transaction with given id; transactionId={}", transactionId);
      throw new BadRequestResponse("Invalid transaction id");
    } catch (DatabaseException ex) {
      LOGGER.error("Database exception during transaction creation", ex);
      throw new InternalServerErrorResponse();
    }
  }

  public void createTransaction(Context context) {
    // validate request body
    final var postTransactionDto = context.bodyValidator(PostTransactionDto.class)
      .check(Objects::nonNull)
      .check(dto -> dto.getAmount() != null && dto.getAmount().compareTo(BigDecimal.ZERO) > 0)
      // six accounts overall
      .check(dto -> dto.getRecipient() > 0 && dto.getRecipient() < 7)
      .check(dto -> dto.getSender() > 0 && dto.getSender() < 7)
      .get();

    try {
      context.json(Map.of("transactionId", transactionService.createTransaction(postTransactionDto)));
      context.status(Response.SC_CREATED);
    } catch (EntityNotFoundException ex) {
      LOGGER.warn("Entity not found exception for {}", postTransactionDto, ex);
      throw new BadRequestResponse("Invalid sender id");
    } catch (NotEnoughMoneyException ex) {
      LOGGER.warn("Not enough money exception for {}", postTransactionDto, ex);
      throw new BadRequestResponse("Not enough money");
    } catch (DatabaseException ex) {
      LOGGER.error("Database exception during transaction creation", ex);
      throw new InternalServerErrorResponse();
    }
  }
}
