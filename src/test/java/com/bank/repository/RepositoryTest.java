package com.bank.repository;

import com.bank.exception.EntityNotFoundException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class RepositoryTest {
  private static final String FIND_TX_BY_ID_QUERY = "SELECT " +
    "transaction_id, creation_time, amount, recipient, sender " +
    "FROM transactions where id=%d;";

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
    final var tx1 = new TransactionDto(1, now, BigDecimal.TEN.setScale(2, RoundingMode.HALF_DOWN), 1, 2);

    final var resultSetMock = mock(ResultSet.class);
    when(resultSetMock.next()).thenReturn(true);
    when(resultSetMock.getInt(1)).thenReturn(tx1.getTransactionId());
    when(resultSetMock.getTimestamp(2)).thenReturn(Timestamp.valueOf(now));
    when(resultSetMock.getBigDecimal(3)).thenReturn(tx1.getAmount());
    when(resultSetMock.getInt(4)).thenReturn(tx1.getSender());
    when(resultSetMock.getInt(5)).thenReturn(tx1.getRecipient());

    when(statement.executeQuery(String.format(FIND_TX_BY_ID_QUERY, tx1.getTransactionId()))).thenReturn(resultSetMock);

    final var repository = new Repository(connection);
    assertEquals(tx1, repository.findTransactionById(tx1.getTransactionId()));
  }

  @Test
  void findTransactionByIdThrows() throws SQLException {
    final var repository = new Repository(connection);
    final var resultSetMock = mock(ResultSet.class);
    when(statement.executeQuery(String.format(FIND_TX_BY_ID_QUERY, 1))).thenReturn(resultSetMock);
    assertThrows(EntityNotFoundException.class, () -> repository.findTransactionById(1));
  }
}