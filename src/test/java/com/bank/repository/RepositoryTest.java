package com.bank.repository;

import com.bank.exception.EntityNotFoundException;
import com.bank.pojo.AccountDto;
import com.bank.pojo.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDateTime;

import static com.bank.repository.Repository.SELECT_ACCOUNT_BY_ID_QUERY;
import static com.bank.repository.Repository.SELECT_TX_BY_ID_QUERY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepositoryTest {

  @Mock
  Connection connection;
  @Mock
  Statement statement;

  @BeforeEach
  void setUp() throws SQLException {
    when(connection.createStatement()).thenReturn(statement);
  }

  @Test
  void findTransactionByIdSuccess() throws SQLException {
    final var now = LocalDateTime.now();
    final var tx1 = new TransactionDto(1, now,
      BigDecimal.TEN.setScale(2, RoundingMode.HALF_DOWN), 1, 2);

    final var resultSetMock = mock(ResultSet.class);
    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getInt(1)).thenReturn(tx1.getTransactionId());
    when(resultSetMock.getTimestamp(2)).thenReturn(Timestamp.valueOf(now));
    when(resultSetMock.getBigDecimal(3)).thenReturn(tx1.getAmount());
    when(resultSetMock.getInt(4)).thenReturn(tx1.getSender());
    when(resultSetMock.getInt(5)).thenReturn(tx1.getRecipient());

    when(statement.executeQuery(String.format(SELECT_TX_BY_ID_QUERY, tx1.getTransactionId())))
      .thenReturn(resultSetMock);

    final var repository = new Repository(connection);
    assertEquals(tx1, repository.findTransactionById(tx1.getTransactionId()));
  }

  @Test
  void findTransactionByIdThrows() throws SQLException {
    final var repository = new Repository(connection);
    final var resultSetMock = mock(ResultSet.class);
    when(statement.executeQuery(String.format(SELECT_TX_BY_ID_QUERY, 1))).thenReturn(resultSetMock);
    assertThrows(EntityNotFoundException.class, () -> repository.findTransactionById(1));
  }

  @Test
  void findAccountByIdSuccess() throws SQLException {
    final var account1 = new AccountDto(1, "123456.12");

    final var resultSetMock = mock(ResultSet.class);
    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getInt(1)).thenReturn(account1.getAccountId());
    when(resultSetMock.getString(2)).thenReturn(account1.getBalance().toString());

    when(statement.executeQuery(String.format(SELECT_ACCOUNT_BY_ID_QUERY, account1.getAccountId())))
      .thenReturn(resultSetMock);

    final var repository = new Repository(connection);
    assertEquals(account1, repository.findAccountById(account1.getAccountId()));
  }

  @Test
  void findAccountByIdThrows() throws SQLException {
    final var repository = new Repository(connection);
    final var resultSetMock = mock(ResultSet.class);
    when(statement.executeQuery(String.format(SELECT_ACCOUNT_BY_ID_QUERY, 1))).thenReturn(resultSetMock);
    assertThrows(EntityNotFoundException.class, () -> repository.findAccountById(1));
  }
}