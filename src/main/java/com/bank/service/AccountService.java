package com.bank.service;

import com.bank.pojo.Account;
import com.bank.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public class AccountService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

  private final Repository repository;

  public AccountService(Repository repository) {
    this.repository = repository;
  }

  public List<Account> getAccountList() {
    try {
      LOGGER.info("Selecting all accounts from repository");
      return repository.selectAllAccounts();
    } catch (SQLException ex) {
      LOGGER.error("SQLException during all accounts select", ex);
      throw new RuntimeException();
    }
  }
}
