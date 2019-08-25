package com.bank.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Account {
  private final int accountId;

  @JsonCreator
  public Account(@JsonProperty("accountId") int accountId) {
    this.accountId = accountId;
  }

  public int getAccountId() {
    return accountId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return accountId == account.accountId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId);
  }

  @Override
  public String toString() {
    return "Account{" +
      "accountId=" + accountId +
      '}';
  }
}
