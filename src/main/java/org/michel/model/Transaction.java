package org.michel.model;

import java.time.LocalDateTime;

public class Transaction {
    private String id;
    private String label;
    private Double amount;
    private LocalDateTime dateTime;
    private TransactionType transactionType;

    public Transaction(String id, String label, Double amount, LocalDateTime dateTime, TransactionType transactionType) {
        this.id = id;
        this.label = label;
        this.amount = amount;
        this.dateTime = dateTime;
        this.transactionType = transactionType;
    }

    public String getId() {
        return id;
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
