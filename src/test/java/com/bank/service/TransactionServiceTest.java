package com.bank.service;

import com.bank.exception.EntityNotFoundException;
import com.bank.pojo.TransactionDto;
import com.bank.repository.Repository;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @Mock
  Repository repository;

  @Test
  void getTransactionByIdSuccess() throws SQLException {
    final var tx1 = new TransactionDto(1, LocalDateTime.now(),
      BigDecimal.TEN.setScale(2, RoundingMode.HALF_DOWN), 1, 2);
    when(repository.findTransactionById(anyInt())).thenReturn(tx1);

    final var transactionService = new TransactionService(repository);
    assertEquals(tx1, transactionService.getTransactionById(tx1.getTransactionId()));
  }

  @Test
  void getTransactionByIdNoTransactionWithGivenId() throws SQLException {
    when(repository.findTransactionById(anyInt())).thenThrow(EntityNotFoundException.class);
    final var transactionService = new TransactionService(repository);
    final var badRequestResponse = assertThrows(BadRequestResponse.class,
      () -> transactionService.getTransactionById(1));
    assertEquals("Invalid transaction id", badRequestResponse.getMessage());
  }

  @Test
  void getTransactionByIdSQLException() throws SQLException {
    when(repository.findTransactionById(anyInt())).thenThrow(SQLException.class);
    final var transactionService = new TransactionService(repository);
    assertThrows(InternalServerErrorResponse.class,
      () -> transactionService.getTransactionById(1));
  }
}