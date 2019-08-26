package com.bank.service;

import com.bank.exception.DatabaseException;
import com.bank.exception.EntityNotFoundException;
import com.bank.exception.NotEnoughMoneyException;
import com.bank.pojo.AccountDto;
import com.bank.pojo.PostTransactionDto;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @Mock
  Repository repository;

  @Test
  void getTransactionByIdSuccess() throws SQLException {
    final var tx1 = new TransactionDto(1, LocalDateTime.now(),
      BigDecimal.TEN.setScale(2, RoundingMode.HALF_DOWN), 1, 2);
    when(repository.findTransactionById(tx1.getTransactionId())).thenReturn(tx1);

    final var transactionService = new TransactionService(repository);
    assertEquals(tx1, transactionService.getTransactionById(tx1.getTransactionId()));
  }

  @Test
  void getTransactionByIdNoTransactionWithGivenId() throws SQLException {
    when(repository.findTransactionById(anyInt())).thenThrow(EntityNotFoundException.class);
    final var transactionService = new TransactionService(repository);
    final var badRequestResponse = assertThrows(EntityNotFoundException.class,
      () -> transactionService.getTransactionById(1));
  }

  @Test
  void getTransactionByIdSQLException() throws SQLException {
    when(repository.findTransactionById(anyInt())).thenThrow(SQLException.class);
    final var transactionService = new TransactionService(repository);
    assertThrows(DatabaseException.class,
      () -> transactionService.getTransactionById(1));
  }

  @Test
  void createTransactionNoSenderThrows() throws SQLException {
    when(repository.findAccountById(anyInt())).thenThrow(EntityNotFoundException.class);
    final var transactionService = new TransactionService(repository);
    assertThrows(EntityNotFoundException.class,
      () -> transactionService.createTransaction(new PostTransactionDto("5", 1, 2)));
  }

  @Test
  void createTransactionNullBalanceThrows() throws SQLException {
    final var account1 = mock(AccountDto.class);
    when(repository.findAccountById(anyInt())).thenReturn(account1);
    final var transactionService = new TransactionService(repository);

    assertThrows(NotEnoughMoneyException.class,
      () -> transactionService.createTransaction(new PostTransactionDto("5", 1, 2)));
  }

  @Test
  void createTransactionNotEnoughMoneyThrows() throws SQLException {
    final var account2 = new AccountDto(2, "0");
    when(repository.findAccountById(account2.getAccountId())).thenReturn(account2);
    final var transactionService = new TransactionService(repository);

    assertThrows(NotEnoughMoneyException.class,
      () -> transactionService.createTransaction(new PostTransactionDto("5", account2.getAccountId(), 2)));
  }

  @Test
  void createTransactionSuccess() throws SQLException {
    final int id = 200;
    final var account2 = new AccountDto(2, "500.00");
    final var dto = new PostTransactionDto("5", account2.getAccountId(), 2);

    when(repository.findAccountById(account2.getAccountId())).thenReturn(account2);
    when(repository.createTransaction(dto)).thenReturn(id);

    assertEquals(200, new TransactionService(repository).createTransaction(dto));
  }
}