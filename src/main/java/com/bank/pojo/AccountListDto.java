package com.bank.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountListDto {
  private final int accountId;

  @JsonCreator
  public AccountListDto(@JsonProperty("accountId") int accountId) {
    this.accountId = accountId;
  }

  public int getAccountId() {
    return accountId;
  }

  @Override
  public String toString() {
    return "AccountListDto{" +
      "accountId=" + accountId +
      '}';
  }
}
