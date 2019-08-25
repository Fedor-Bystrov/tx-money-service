package com.bank.repository;

import com.bank.exception.EntityNotFoundException;
import com.bank.pojo.TransactionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;

public class Repository {
  private static final Logger LOGGER = LoggerFactory.getLogger(Repository.class);
  private static final String FIND_TX_BY_ID_QUERY = "SELECT " +
    "transaction_id, creation_time, amount, recipient, sender " +
    "FROM transactions where transaction_id=%d;";

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
         final var resultSet = statement.executeQuery(String.format(FIND_TX_BY_ID_QUERY, transactionId))) {

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
}
