package org.michel.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private final int account_id;
    private final String name;
    private Balance balance;
    private List<Transaction> transactions;
    private Currency currency;
    private AccountType type;

    public Account(int account_id, String name, Balance balance, List<Transaction> transactions, Currency currency, AccountType type) {
        this.account_id = account_id;
        this.name = name;
        this.balance = balance;
        this.transactions = transactions;
        this.currency = currency;
        this.type = type;
    }

    public int getAccount_id() {
        return account_id;
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

    // Fonction pour effectuer une transaction
    public Account performTransaction(String label, double amount, TransactionType transactionType) {
        // Création d'une nouvelle transaction avec la date actuelle
        LocalDateTime dateTime = LocalDateTime.now();
        Transaction newTransaction = new Transaction(String.valueOf(transactions.size() + 1), label, amount, dateTime, transactionType);

        // Mise à jour du solde en fonction du type de transaction
        if (transactionType == TransactionType.DEBIT && type != AccountType.BANQUE) {
            // Vérification de la suffisance du solde pour les comptes autres que la banque
            if (balance.getAmount() < amount) {
                throw new IllegalArgumentException("Solde insuffisant pour la transaction débit");
            }
            balance.setAmount(balance.getAmount() - amount);
        } else if (transactionType == TransactionType.CREDIT) {
            balance.setAmount(balance.getAmount() + amount);
        }

        // Ajout de la nouvelle transaction à la liste
        transactions.add(newTransaction);

        // Retour du compte mis à jour
        return this;
    }

    // fonction qui permet d’obtenir le solde d’un compte à une date et heure donnée
    public double getBalanceAtDateTime(LocalDateTime dateTime) {
        double balanceAtDateTime = balance.getAmount();

        // Parcourir la liste des transactions pour ajuster le solde
        for (Transaction transaction : transactions) {
            if (transaction.getDateTime().isBefore(dateTime) || transaction.getDateTime().isEqual(dateTime)) {
                if (transaction.getTransactionType() == TransactionType.DEBIT && type != AccountType.BANQUE) {
                    balanceAtDateTime -= transaction.getAmount();
                } else if (transaction.getTransactionType() == TransactionType.CREDIT) {
                    balanceAtDateTime += transaction.getAmount();
                }
            }
        }

        return balanceAtDateTime;
    }

    // Fonction pour obtenir l'historique du solde dans une plage de date et heure
    public List<Double> getBalanceHistory(LocalDateTime startDate, LocalDateTime endDate) {
        List<Double> balanceHistory = new ArrayList<>();
        double currentBalance = balance.getAmount();

        // Parcourir la liste des transactions dans l'intervalle spécifié
        for (Transaction transaction : transactions) {
            if (transaction.getDateTime().isAfter(startDate) && transaction.getDateTime().isBefore(endDate.plusSeconds(1))) {
                // Ajouter le solde actuel avant la transaction à l'historique
                balanceHistory.add(currentBalance);

                // Mettre à jour le solde en fonction de la transaction
                if (transaction.getTransactionType() == TransactionType.DEBIT && type != AccountType.BANQUE) {
                    currentBalance -= transaction.getAmount();
                } else if (transaction.getTransactionType() == TransactionType.CREDIT) {
                    currentBalance += transaction.getAmount();
                }
            }
        }

        // Ajouter le solde final à l'historique
        balanceHistory.add(currentBalance);

        return balanceHistory;
    }
}