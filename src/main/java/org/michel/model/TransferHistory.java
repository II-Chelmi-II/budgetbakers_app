package org.michel.model;

import java.time.LocalDateTime;

public class TransferHistory {
    private final String historyId;
    private final int debitTransactionId;
    private final String creditTransactionId;
    private final LocalDateTime dateTime;

    public TransferHistory(String historyId, int debitTransactionId, String creditTransactionId, LocalDateTime dateTime) {
        this.historyId = historyId;
        this.debitTransactionId = debitTransactionId;
        this.creditTransactionId = creditTransactionId;
        this.dateTime = dateTime;
    }

    public String getHistoryId() {
        return historyId;
    }

    public int getDebitTransactionId() {
        return debitTransactionId;
    }

    public String getCreditTransactionId() {
        return creditTransactionId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

}