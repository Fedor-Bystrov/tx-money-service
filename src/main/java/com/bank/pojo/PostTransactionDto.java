package com.bank.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PostTransactionDto {
  private BigDecimal amount;
  private int sender;
  private int recipient;

  @JsonCreator
  public PostTransactionDto(@JsonProperty("amount") String amount,
                            @JsonProperty("sender") int sender,
                            @JsonProperty("recipient") int recipient) {
    this.amount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_DOWN);
    this.sender = sender;
    this.recipient = recipient;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public int getSender() {
    return sender;
  }

  public int getRecipient() {
    return recipient;
  }

  @Override
  public String toString() {
    return "PostTransactionDto{" +
      "amount=" + amount +
      ", sender=" + sender +
      ", recipient=" + recipient +
      '}';
  }
}
