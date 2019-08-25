package com.bank.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class TransactionDto {
  private int transactionId;
  private LocalDateTime creationTime;
  private BigDecimal amount;
  private int sender;
  private int recipient;

  @JsonCreator
  public TransactionDto(@JsonProperty("transactionId") int transactionId,
                        @JsonProperty("creationTime") LocalDateTime creationTime,
                        @JsonProperty("amount") BigDecimal amount,
                        @JsonProperty("sender") int sender,
                        @JsonProperty("recipient") int recipient) {
    this.transactionId = transactionId;
    this.creationTime = creationTime;
    this.amount = amount;
    this.sender = sender;
    this.recipient = recipient;
  }

  public int getTransactionId() {
    return transactionId;
  }

  public LocalDateTime getCreationTime() {
    return creationTime;
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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    TransactionDto that = (TransactionDto) o;
    return transactionId == that.transactionId &&
      sender == that.sender &&
      recipient == that.recipient &&
      creationTime.equals(that.creationTime) &&
      amount.equals(that.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, creationTime, amount, sender, recipient);
  }

  @Override
  public String toString() {
    return "TransactionDto{" +
      "transactionId=" + transactionId +
      ", creationTime=" + creationTime +
      ", amount=" + amount +
      ", sender=" + sender +
      ", recipient=" + recipient +
      '}';
  }
}
