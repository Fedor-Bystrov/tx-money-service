package com.bank.service;

import com.bank.entity.Account;
import com.bank.repository.AppRepository;

import java.util.Collections;
import java.util.List;

public class AccountService {
  private final AppRepository repository;

  public AccountService(AppRepository repository) {
    this.repository = repository;
  }

  public List<Account> getAllAccounts() {
    return Collections.emptyList();
  }
}
