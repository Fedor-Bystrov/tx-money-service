package com.bank.db;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;

public class Functions {
  private static final String SELECT_SENT_AMOUNT = "SELECT IFNULL(sum(amount), 0) " +
    "FROM transactions WHERE sender = %d";
  private static final String SELECT_RECEIVED_AMOUNT = "SELECT IFNULL(sum(amount), 0) " +
    "FROM transactions WHERE recipient = %d";

  /**
   * H2 analogous of stored procedure.
   * Returns balance of account given accountId
   */
  public static BigDecimal getAccountBalance(Connection connection, int accountId) throws SQLException {
    try (final var statement = connection.createStatement()) {
      // select sent amount
      final var sentResultSet = statement.executeQuery(String.format(SELECT_SENT_AMOUNT, accountId));
      final var sent = sentResultSet.next() ? sentResultSet.getBigDecimal(1) : BigDecimal.ZERO;
      sentResultSet.close();

      // select received amount
      final var receivedResultSet = statement.executeQuery(String.format(SELECT_RECEIVED_AMOUNT, accountId));
      final var received = receivedResultSet.next() ? receivedResultSet.getBigDecimal(1) : BigDecimal.ZERO;
      receivedResultSet.close();

      return received.subtract(sent).setScale(2, RoundingMode.HALF_DOWN);
    }
  }
}
