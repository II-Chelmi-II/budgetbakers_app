package org.michel.model;

import java.time.LocalDateTime;

public class Transaction {
    private final int transaction_id;
    private final String label;
    private final Double amount;
    private final LocalDateTime dateTime;
    private final TransactionType transactionType;

    public Transaction(int transaction_id, String label, Double amount, LocalDateTime dateTime, TransactionType transactionType) {
        this.transaction_id = transaction_id;
        this.label = label;
        this.amount = amount;
        this.dateTime = dateTime;
        this.transactionType = transactionType;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public String getLabel() {
        return label;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
}
