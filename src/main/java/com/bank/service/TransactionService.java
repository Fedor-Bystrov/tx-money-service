package com.bank.service;

import com.bank.exception.DatabaseException;
import com.bank.exception.EntityNotFoundException;
import com.bank.exception.NotEnoughMoneyException;
import com.bank.pojo.PostTransactionDto;
import com.bank.pojo.TransactionDto;
import com.bank.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class TransactionService {
  private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

  private final Repository repository;

  public TransactionService(Repository repository) {
    this.repository = repository;
  }

  /**
   * Method for selecting transaction details given transactionId
   *
   * @throws EntityNotFoundException if no transaction with specified id
   * @throws DatabaseException       if database driver returned an exception
   */
  public TransactionDto getTransactionById(int transactionId) {
    try {
      return repository.findTransactionById(transactionId);
    } catch (SQLException ex) {
      LOGGER.error("SQLException, cannot fetch transaction by id: transactionId={}; Exception: ",
        transactionId, ex);
      throw new DatabaseException(ex);
    }
  }

  /**
   * Method for transaction creation, checks that sender has enough money.
   *
   * @param dto new transaction data
   * @return id of created transaction
   * @throws EntityNotFoundException if no sender with given id
   * @throws NotEnoughMoneyException if sender with given id does not have enough money
   * @throws DatabaseException       if database driver returned an exception
   */
  public int createTransaction(PostTransactionDto dto) {
    try {
      // check that sender has enough money
      final var senderAccount = repository.findAccountById(dto.getSender());
      if (senderAccount.getBalance() == null || senderAccount.getBalance().compareTo(dto.getAmount()) < 0) {
        throw new NotEnoughMoneyException();
      }

      return repository.createTransaction(dto);
    } catch (SQLException ex) {
      LOGGER.error("SQLException, cannot create transaction dto={}; Exception: ", dto, ex);
      throw new DatabaseException(ex);
    }
  }
}
