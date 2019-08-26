package com.bank.service;

import com.bank.exception.DatabaseException;
import com.bank.exception.EntityNotFoundException;
import com.bank.pojo.AccountDto;
import com.bank.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class AccountService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

  private final Repository repository;

  public AccountService(Repository repository) {
    this.repository = repository;
  }

  /**
   * Method for selecting account information given accountId
   *
   * @param accountId id of account
   * @return AccountDto with accountId and balance
   * @throws EntityNotFoundException if no account with specified id
   * @throws DatabaseException       if database driver returned an exception
   */
  public AccountDto getAccountById(int accountId) {
    try {
      return repository.findAccountById(accountId);
    } catch (SQLException ex) {
      LOGGER.error("SQLException, cannot fetch account by id: accountId={}; Exception: ",
        accountId, ex);
      throw new DatabaseException(ex);
    }
  }
}
