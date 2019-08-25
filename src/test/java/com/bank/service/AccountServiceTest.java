package com.bank.service;

import com.bank.exception.EntityNotFoundException;
import com.bank.pojo.AccountDto;
import com.bank.repository.Repository;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.InternalServerErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

  @Mock
  Repository repository;

  @Test
  void getAccountByIdSuccess() throws SQLException {
    final var account1 = new AccountDto(1, "12345.67");
    when(repository.findAccountById(account1.getAccountId())).thenReturn(account1);

    final var accountService = new AccountService(repository);
    assertEquals(account1, accountService.getAccountById(account1.getAccountId()));
  }

  @Test
  void getAccountByIdNoAccountWithGivenId() throws SQLException {
    when(repository.findAccountById(anyInt())).thenThrow(EntityNotFoundException.class);
    final var accountService = new AccountService(repository);
    final var badRequestResponse = assertThrows(BadRequestResponse.class,
      () -> accountService.getAccountById(1));
    assertEquals("Invalid account id", badRequestResponse.getMessage());
  }

  @Test
  void getAccountByIdSQLException() throws SQLException {
    when(repository.findAccountById(anyInt())).thenThrow(SQLException.class);
    final var accountService = new AccountService(repository);
    assertThrows(InternalServerErrorResponse.class,
      () -> accountService.getAccountById(1));
  }
}