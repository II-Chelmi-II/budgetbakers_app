package org.michel.model;

import java.util.List;

public class Account {
    private String id;
    private String name;
    private Balance balance;
    private List<Transaction> transactions;
    private Currency currency;
    private AccountType type;

    public Account(String id, String name, Balance balance, List<Transaction> transactions, Currency currency, AccountType type) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.transactions = transactions;
        this.currency = currency;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AccountType getType() {
        return type;
    }
}
