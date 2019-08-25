package com.bank.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class AccountDto {
  private final int accountId;
  private final BigDecimal balance;

  @JsonCreator
  public AccountDto(@JsonProperty("accountId") int accountId,
                    @JsonProperty("balance") String balance) {
    this.accountId = accountId;
    this.balance = new BigDecimal(balance).setScale(2, RoundingMode.HALF_DOWN);
  }

  public int getAccountId() {
    return accountId;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AccountDto that = (AccountDto) o;
    return accountId == that.accountId &&
      balance.equals(that.balance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, balance);
  }

  @Override
  public String toString() {
    return "AccountDto{" +
      "accountId=" + accountId +
      ", balance=" + balance +
      '}';
  }
}
