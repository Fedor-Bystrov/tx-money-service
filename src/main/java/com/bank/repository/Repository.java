package com.bank.repository;

import com.bank.pojo.AccountListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Repository {
  private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);
  private static final String ALL_ACCOUNTS_QUERY = "SELECT account_id FROM accounts;";

  private final Connection connection;

  public Repository(Connection connection) {
    this.connection = connection;
  }

  public List<AccountListDto> selectAllAccounts() throws SQLException {
    LOGGER.info("Selecting all accounts");
    try (final var statement = connection.createStatement();
         final var resultSet = statement.executeQuery(ALL_ACCOUNTS_QUERY)) {

      LOGGER.info("Extracting accounts");
      final var accounts = new ArrayList<AccountListDto>();
      while (resultSet.next()) {
        accounts.add(new AccountListDto(resultSet.getInt(1)));
      }

      return accounts;
    }
  }
}
