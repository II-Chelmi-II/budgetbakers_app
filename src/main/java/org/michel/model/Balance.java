package org.michel.model;

public class Balance {
    private double amount;
    private String lastUpdateDate;

    public Balance(double amount, String lastUpdateDate) {
        this.amount = amount;
        this.lastUpdateDate = lastUpdateDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}