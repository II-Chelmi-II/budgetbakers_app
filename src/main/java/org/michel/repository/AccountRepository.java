package org.michel.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.michel.ConnectionDB;
import org.michel.model.Account;
import org.michel.model.AccountType;
import org.michel.model.Balance;
import org.michel.model.Currency;

public class AccountRepository {
    public void insertAccount(Account account) {
        try (Connection connection = ConnectionDB.getConnection()) {
            String sql = "INSERT INTO accounts (account_id, name, balance_amount, balance_last_update_date, currency_id, type) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, account.getAccount_id());
                statement.setString(2, account.getName());
                statement.setDouble(3, account.getBalance().getAmount());
                statement.setTimestamp(4, Timestamp.valueOf(account.getBalance().getLastUpdateDate()));
                statement.setInt(5, account.getCurrency().getCurrency_id());
                statement.setString(6, account.getType().name());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getAccountById(int accountId) {
        try (Connection connection = ConnectionDB.getConnection()) {
            String sql = "SELECT * FROM Account WHERE account_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, accountId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return mapAccount(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = ConnectionDB.getConnection()) {
            String sql = "SELECT * FROM accounts";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    accounts.add(mapAccount(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    private Account mapAccount(ResultSet resultSet) throws SQLException {

        int accountId = resultSet.getInt("account_id");
        String name = resultSet.getString("name");
        double balanceAmount = resultSet.getDouble("balance_amount");
        Timestamp balanceLastUpdateDate = resultSet.getTimestamp("balance_last_update_date");
        int currencyId = resultSet.getInt("currency_id");
        String type = resultSet.getString("type");

        return new Account(accountId, name, new Balance(balanceAmount, balanceLastUpdateDate.toLocalDateTime().toString()),
                new ArrayList<>(), new Currency(currencyId, null, null), AccountType.valueOf(type));
    }
}

