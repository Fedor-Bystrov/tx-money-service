package com.bank.service;

import com.bank.exception.EntityNotFoundException;
import com.bank.pojo.TransactionDto;
import com.bank.repository.Repository;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class TransactionService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

  private final Repository repository;

  public TransactionService(Repository repository) {
    this.repository = repository;
  }

  public TransactionDto getTransactionById(int transactionId) {
    try {
      return repository.findTransactionById(transactionId);
    } catch (EntityNotFoundException ex) {
      LOGGER.info("No transaction with given id; transactionId={}", transactionId);
      throw new BadRequestResponse("Invalid transaction id");
    } catch (SQLException ex) {
      LOGGER.error("SQLException, cannot fetch transaction by id: transactionId={}; Exception: ",
        transactionId, ex);
      throw new InternalServerErrorResponse();
    }
  }
}
