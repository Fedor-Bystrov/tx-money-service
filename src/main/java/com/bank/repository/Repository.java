package com.bank.repository;

import com.bank.exception.EntityNotFoundException;
import com.bank.pojo.AccountDto;
import com.bank.pojo.PostTransactionDto;
import com.bank.pojo.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Repository {
  private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);
  static final String SELECT_ACCOUNT_BY_ID_QUERY = "SELECT " +
    "account_id, (select get_account_balance(account_id)) " +
    "FROM accounts WHERE account_id = %d;";
  static final String SELECT_TX_BY_ID_QUERY = "SELECT " +
    "transaction_id, creation_time, amount, sender, recipient  " +
    "FROM transactions WHERE transaction_id=%d;";
  static final String INSERT_TX_QUERY =
    "INSERT INTO transactions (amount, recipient, sender) VALUES (?, ?, ?);";

  private final Connection connection;

  public Repository(Connection connection) {
    this.connection = connection;
  }

  /**
   * Select transaction by {@code transactionId} from database.
   *
   * @throws EntityNotFoundException if no transaction with specified id
   */
  public TransactionDto findTransactionById(int transactionId) throws SQLException {
    LOGGER.info("Looking for transaction with id = {}", transactionId);
    try (final var statement = connection.createStatement();
         final var resultSet = statement.executeQuery(String.format(SELECT_TX_BY_ID_QUERY, transactionId))) {

      if (resultSet.next()) {
        return new TransactionDto(resultSet.getInt(1),
          resultSet.getTimestamp(2).toLocalDateTime(),
          resultSet.getBigDecimal(3).setScale(2, RoundingMode.HALF_DOWN),
          resultSet.getInt(4),
          resultSet.getInt(5)
        );
      } else {
        throw new EntityNotFoundException();
      }
    }
  }

  /**
   * Select account by {@code accountId} from database,
   * calculates and returns account.balance as well
   *
   * @throws EntityNotFoundException if no account with specified id
   */
  public AccountDto findAccountById(int accountId) throws SQLException {
    LOGGER.info("Looking for account with id = {}", accountId);
    try (final var statement = connection.createStatement();
         final var resultSet = statement.executeQuery(String.format(SELECT_ACCOUNT_BY_ID_QUERY, accountId))) {

      if (resultSet.next()) {
        return new AccountDto(resultSet.getInt(1), resultSet.getString(2));
      } else {
        throw new EntityNotFoundException();
      }
    }
  }

  /**
   * Creates new transaction given {@code PostTransactionDto}, returns id of created transaction
   */
  public int createTransaction(PostTransactionDto dto) throws SQLException {
    LOGGER.info("Creating new transaction given: {}", dto);
    try (final var statement = connection.prepareStatement(INSERT_TX_QUERY, Statement.RETURN_GENERATED_KEYS)) {
      statement.setBigDecimal(1, dto.getAmount());
      statement.setInt(2, dto.getRecipient());
      statement.setInt(3, dto.getSender());
      statement.executeUpdate();

      final var generatedKeys = statement.getGeneratedKeys();
      if (generatedKeys.next()) {
        return generatedKeys.getInt(1);
      } else {
        LOGGER.error("New transaction creation error, cannot return id of created transaction");
        throw new SQLException("Cannot retrieve id of created transaction");
      }
    }
  }
}
