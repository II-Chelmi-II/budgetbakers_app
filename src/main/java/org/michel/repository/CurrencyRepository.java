package org.michel.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.michel.ConnectionDB;
import org.michel.model.Currency;

public class CurrencyRepository {
    public void insertCurrency(Currency currency) {
        try (Connection connection = ConnectionDB.getConnection()) {
            String sql = "INSERT INTO Currency (currency_id, name, code) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, currency.getCurrency_id());
                statement.setString(2, currency.getName());
                statement.setString(3, currency.getCode());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Currency getCurrencyById(int currencyId) {
        try (Connection connection = ConnectionDB.getConnection()) {
            String sql = "SELECT * FROM Currency WHERE currency_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, currencyId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return mapCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = ConnectionDB.getConnection()) {
            String sql = "SELECT * FROM Currency";
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    currencies.add(mapCurrency(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencies;
    }

    private Currency mapCurrency(ResultSet resultSet) throws SQLException {

        int currency_id = resultSet.getInt("currency_id");
        String name = resultSet.getString("name");
        String code = resultSet.getString("code");

        return new Currency(currency_id, name, code);
    }
}
