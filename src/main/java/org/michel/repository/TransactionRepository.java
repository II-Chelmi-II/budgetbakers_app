package org.michel.repository;

import org.michel.ConnectionDB;
import org.michel.model.Account;
import org.michel.model.Transaction;
import org.michel.model.TransactionType;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    public void insertTransaction(Transaction transaction, int accountId) {
        try (Connection connection = ConnectionDB.getConnection()) {
            String sql = "INSERT INTO Transaction (transaction_id, label, amount, date_time, transaction_type, account_id) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, transaction.getTransaction_id());
                statement.setString(2, transaction.getLabel());
                statement.setDouble(3, transaction.getAmount());
                statement.setTimestamp(4, Timestamp.valueOf(transaction.getDateTime()));
                statement.setString(5, transaction.getTransactionType().name());
                statement.setInt(6, accountId);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getTransactionsByAccountId(int accountId) {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection connection = ConnectionDB.getConnection()) {
            String sql = "SELECT * FROM Transaction WHERE account_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, accountId);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    transactions.add(mapTransaction(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private Transaction mapTransaction(ResultSet resultSet) throws SQLException {

        int transactionId = resultSet.getInt("transaction_id");
        String label = resultSet.getString("label");
        double amount = resultSet.getDouble("amount");
        LocalDateTime dateTime = resultSet.getTimestamp("date_time").toLocalDateTime();
        String transactionType = resultSet.getString("transaction_type");

        return new Transaction(transactionId, label, amount, dateTime, TransactionType.valueOf(transactionType));
    }


    // fonction Java qui n’utilise pas la fonction SQL de la question 2) mais qui retourne le même résultat
    public double getSumOfEntriesAndExits(Account account, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try (Connection connection = ConnectionDB.getConnection()) {
            String sql = "SELECT SUM(CASE WHEN transaction_type = 'CREDIT' THEN amount ELSE -amount END) AS sum_amount FROM Transaction WHERE account_id = ? AND date_time BETWEEN ? AND ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, account.getAccount_id());
                statement.setTimestamp(2, Timestamp.valueOf(startDateTime));
                statement.setTimestamp(3, Timestamp.valueOf(endDateTime));
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getDouble("sum_amount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double getSumOfCategories(Account account, LocalDateTime startDateTime, LocalDateTime endDateTime, Account.TransactionCategory category) {
        try (Connection connection = ConnectionDB.getConnection()) {
            String sql = "SELECT COALESCE(SUM(CASE WHEN category_id = ? THEN amount ELSE 0 END), 0) AS sum_amount FROM Transaction WHERE account_id = ? AND date_time BETWEEN ? AND ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, category.ordinal()); // Assuming ordinal represents category_id in the database
                statement.setInt(2, account.getAccount_id());
                statement.setTimestamp(3, Timestamp.valueOf(startDateTime));
                statement.setTimestamp(4, Timestamp.valueOf(endDateTime));
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getDouble("sum_amount");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
