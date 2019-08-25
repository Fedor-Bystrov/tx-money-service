package com.bank.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class TransactionDto {
  private int transactionId;
  private LocalDateTime creationTime;
  private BigDecimal amount;
  private int sender;
  private int recipient;

  public TransactionDto(int transactionId, LocalDateTime creationTime, BigDecimal amount, int sender, int recipient) {
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
