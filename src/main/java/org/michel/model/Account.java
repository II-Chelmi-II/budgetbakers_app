package org.michel.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Account {
    private final int account_id;
    private final String name;
    private Balance balance;
    private List<Transaction> transactions = new ArrayList<>();
    private final Currency currency;
    private final AccountType type;

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

    private final List<TransferHistory> transferHistoryList = new ArrayList<>();

    public List<TransferHistory> getTransferHistory(LocalDateTime startDate, LocalDateTime endDate) {
        List<TransferHistory> filteredHistory = new ArrayList<>();

        for (TransferHistory transferHistory : transferHistoryList) {
            LocalDateTime transferDate = transferHistory.getDateTime();

            if (transferDate.isAfter(startDate) && transferDate.isBefore(endDate.plusSeconds(1))) {
                filteredHistory.add(transferHistory);
            }
        }

        return filteredHistory;
    }

    // Fonction pour effectuer une transaction
    public Account performTransaction(String label, double amount, TransactionType transactionType) {
        LocalDateTime dateTime = LocalDateTime.now();
        Transaction newTransaction = createTransaction(label, amount, dateTime, transactionType);

        try {
            updateBalance(newTransaction);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Erreur lors de la transaction : " + e.getMessage());
        }

        if (transactionType == TransactionType.DEBIT && type != AccountType.BANQUE) {
            addTransferHistory(newTransaction.getTransaction_id(), dateTime);
        }

        return this;
    }

    private Transaction createTransaction(String label, double amount, LocalDateTime dateTime, TransactionType transactionType) {
        return new Transaction(
                transactions.size() + 1,
                label,
                amount,
                dateTime,
                transactionType
        );
    }

    private void updateBalance(Transaction transaction) {
        if (transaction.getTransactionType() == TransactionType.DEBIT && type != AccountType.BANQUE) {
            if (balance.getAmount() < transaction.getAmount()) {
                throw new IllegalArgumentException("Solde insuffisant pour la transaction débit");
            }
            balance.setAmount(balance.getAmount() - transaction.getAmount());
        } else if (transaction.getTransactionType() == TransactionType.CREDIT) {
            balance.setAmount(balance.getAmount() + transaction.getAmount());
        }
    }

    private void addTransferHistory(int transactionId, LocalDateTime dateTime) {
        TransferHistory transferHistory = new TransferHistory(
                generateUniqueTransferHistoryId(),
                transactionId,
                null,  // ID de la transaction créditeur (non disponible pour le débit)
                dateTime
        );

        transferHistoryList.add(transferHistory);
    }

    private String generateUniqueTransferHistoryId() {
        return UUID.randomUUID().toString();
    }

    // Exception standard pour les erreurs de transaction
    public static class TransactionException extends RuntimeException {
        public TransactionException(String message) {
            super(message);
        }
    }

    // fonction qui permet d’obtenir le solde d’un compte à une date et heure données
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

    // Fonction qui permet de faire un transfert d’argent entre deux comptes
    public static void transferMoney(Account sourceAccount, Account targetAccount, double amount) {
        if (sourceAccount.equals(targetAccount)) {
            throw new IllegalArgumentException("Un compte ne peut pas effectuer un transfert vers lui-même.");
        }

        // Effectuer le transfert depuis le compte source
        sourceAccount.performTransaction("Transfert sortant vers " + targetAccount.getName(), amount, TransactionType.DEBIT);

        // Effectuer le transfert vers le compte cible
        targetAccount.performTransaction("Transfert entrant depuis " + sourceAccount.getName(), amount, TransactionType.CREDIT);
    }
}
