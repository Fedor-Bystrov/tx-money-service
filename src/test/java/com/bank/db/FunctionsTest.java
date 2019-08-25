package com.bank.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.bank.db.Functions.SELECT_RECEIVED_AMOUNT;
import static com.bank.db.Functions.SELECT_SENT_AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FunctionsTest {

  @Mock
  Connection connection;
  @Mock
  Statement statement;

  @BeforeEach
  void setUp() throws SQLException {
    when(connection.createStatement()).thenReturn(statement);
  }

  @Test
  void getAccountBalanceNoTransactions() throws SQLException {
    final int accountId = 1;

    final var sentResultSetMock = mock(ResultSet.class);
    when(sentResultSetMock.next()).thenReturn(true);
    when(sentResultSetMock.getBigDecimal(accountId)).thenReturn(BigDecimal.ZERO);

    final var receivedResultSetMock = mock(ResultSet.class);
    when(receivedResultSetMock.next()).thenReturn(true);
    when(receivedResultSetMock.getBigDecimal(accountId)).thenReturn(BigDecimal.ZERO);

    when(statement.executeQuery(String.format(SELECT_SENT_AMOUNT, accountId))).thenReturn(sentResultSetMock);
    when(statement.executeQuery(String.format(SELECT_RECEIVED_AMOUNT, accountId))).thenReturn(receivedResultSetMock);

    assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN), Functions.getAccountBalance(connection, accountId));

    when(sentResultSetMock.next()).thenReturn(false);
    when(receivedResultSetMock.next()).thenReturn(false);

    assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN), Functions.getAccountBalance(connection, accountId));
  }

  @Test
  void getAccountBalanceOneReceiveTransaction() throws SQLException {
    final int accountId = 1;

    final var sentResultSetMock = mock(ResultSet.class);
    when(sentResultSetMock.next()).thenReturn(true);
    when(sentResultSetMock.getBigDecimal(accountId)).thenReturn(BigDecimal.ZERO);

    final var receivedResultSetMock = mock(ResultSet.class);
    when(receivedResultSetMock.next()).thenReturn(true);
    when(receivedResultSetMock.getBigDecimal(accountId)).thenReturn(BigDecimal.TEN);

    when(statement.executeQuery(String.format(SELECT_SENT_AMOUNT, accountId))).thenReturn(sentResultSetMock);
    when(statement.executeQuery(String.format(SELECT_RECEIVED_AMOUNT, accountId))).thenReturn(receivedResultSetMock);

    assertEquals(BigDecimal.TEN.setScale(2, RoundingMode.HALF_DOWN), Functions.getAccountBalance(connection, accountId));

    when(sentResultSetMock.next()).thenReturn(false);

    assertEquals(BigDecimal.TEN.setScale(2, RoundingMode.HALF_DOWN), Functions.getAccountBalance(connection, accountId));
  }

  @Test
  void getAccountBalance() throws SQLException {
    final int accountId = 1;

    final var sentResultSetMock = mock(ResultSet.class);
    when(sentResultSetMock.next()).thenReturn(true);
    when(sentResultSetMock.getBigDecimal(accountId)).thenReturn(BigDecimal.TEN);

    final var receivedResultSetMock = mock(ResultSet.class);
    when(receivedResultSetMock.next()).thenReturn(true);
    when(receivedResultSetMock.getBigDecimal(accountId)).thenReturn(new BigDecimal("123.25"));

    when(statement.executeQuery(String.format(SELECT_SENT_AMOUNT, accountId))).thenReturn(sentResultSetMock);
    when(statement.executeQuery(String.format(SELECT_RECEIVED_AMOUNT, accountId))).thenReturn(receivedResultSetMock);

    assertEquals(new BigDecimal("113.25").setScale(2, RoundingMode.HALF_DOWN),
      Functions.getAccountBalance(connection, accountId));
  }
}