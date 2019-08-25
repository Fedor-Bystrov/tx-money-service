package com.bank.service;

import com.bank.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

  private final Repository repository;

  public AccountService(Repository repository) {
    this.repository = repository;
  }
}
