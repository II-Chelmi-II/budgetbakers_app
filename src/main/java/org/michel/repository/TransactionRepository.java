package org.michel.repository;

import org.michel.ConnectionDB;
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
}
