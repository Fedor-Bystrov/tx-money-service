package com.bank.service;

import com.bank.exception.EntityNotFoundException;
import com.bank.pojo.AccountDto;
import com.bank.repository.Repository;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class AccountService {
  private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

  private final Repository repository;

  public AccountService(Repository repository) {
    this.repository = repository;
  }

  public AccountDto getAccountById(int accountId) {
    try {
      return repository.findAccountById(accountId);
    } catch (EntityNotFoundException ex) {
      LOGGER.info("No account with given id; accountId={}", accountId);
      throw new BadRequestResponse("Invalid account id");
    } catch (SQLException ex) {
      LOGGER.error("SQLException, cannot fetch account by id: accountId={}; Exception: ",
        accountId, ex);
      throw new InternalServerErrorResponse();
    }
  }
}
