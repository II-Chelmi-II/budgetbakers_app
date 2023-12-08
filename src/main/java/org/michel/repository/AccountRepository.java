package org.michel.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.michel.model.Account;
import org.michel.model.AccountType;
import org.michel.model.Balance;

public class AccountRepository {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/votre_base_de_donnees";
    private static final String USER = "votre_utilisateur";
    private static final String PASSWORD = "votre_mot_de_passe";

    public void insertAccount(Account account) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String sql = "INSERT INTO Account (id, name, balance_amount, balance_last_update_date, currency_id, type) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, account.getId());
                statement.setString(2, account.getName());
                statement.setDouble(3, account.getBalance().getAmount());
                statement.setTimestamp(4, Timestamp.valueOf(account.getBalance().getLastUpdateDate()));
                // Assuming currency_id is an INT in the database
                statement.setInt(5, account.getCurrency().getId());
                statement.setString(6, account.getType().name());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getAccountById(int accountId) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM Account WHERE id = ?";
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
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM Account";
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

    public void updateAccount(Account account) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {
            String sql = "UPDATE Account SET name = ?, balance_amount = ?, balance_last_update_date = ?, currency_id = ?, type = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, account.getName());
                statement.setDouble(2, account.getBalance().getAmount());
                statement.setTimestamp(3, Timestamp.valueOf(account.getBalance().getLastUpdateDate()));
                statement.setInt(4, account.getCurrency().getId());
                statement.setString(5, account.getType().name());
                statement.setInt(6, account.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Account mapAccount(ResultSet resultSet) throws SQLException {
        // Mapping des colonnes du résultat vers un objet Account
        // Assurez-vous d'ajuster cela en fonction de votre modèle de données
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        double balanceAmount = resultSet.getDouble("balance_amount");
        Timestamp balanceLastUpdateDate = resultSet.getTimestamp("balance_last_update_date");
        int currencyId = resultSet.getInt("currency_id");
        String type = resultSet.getString("type");

        // Création et retour de l'objet Account
        return new Account(id, name, new Balance(balanceAmount, balanceLastUpdateDate.toLocalDateTime().toString()),
                new ArrayList<>(), null, AccountType.valueOf(type));
    }
}
