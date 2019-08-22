package com.bank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private long transactionId;
    private LocalDateTime creationTime;
    private BigDecimal amount;
    private Account sender;
    private Account recipient;

    public Transaction(long transactionId, BigDecimal amount, Account sender, Account recipient) {
        this.transactionId = transactionId;
        this.creationTime = LocalDateTime.now();
        this.amount = amount;
        this.sender = sender;
        this.recipient = recipient;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Account getSender() {
        return sender;
    }

    public Account getRecipient() {
        return recipient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", creationTime=" + creationTime +
                ", amount=" + amount +
                ", sender=" + sender +
                ", recipient=" + recipient +
                '}';
    }
}
