CREATE TABLE IF NOT EXISTS Currency (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    code VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS Account (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    balance_amount DOUBLE,
    balance_last_update_date DATETIME,
    currency_id INT,
    type VARCHAR(20),
    FOREIGN KEY (currency_id) REFERENCES Currency(id)
);

CREATE TABLE IF NOT EXISTS Transaction (
    id INT PRIMARY KEY,
    label VARCHAR(100),
    amount DOUBLE,
    date_time DATETIME,
    transaction_type VARCHAR(10),
    account_id INT,
    FOREIGN KEY (account_id) REFERENCES Account(id)
);

-- INSERT devises
INSERT INTO Currency (currency_id, name, code)
VALUES (1, 'Euro', 'EUR');

INSERT INTO Currency (currency_id, name, code)
VALUES (2, 'Ariary', 'MGA');

-- INSERT comptes
INSERT INTO Account (account_id, name, balance_amount, balance_last_update_date, currency_id, type)
VALUES (1, 'Compte Courant', 1000.00, NOW(), 1, 'BANQUE');

INSERT INTO Account (account_id, name, balance_amount, balance_last_update_date, currency_id, type)
VALUES (2, 'Compte Épargne', 500.00, NOW(), 1, 'BANQUE');

INSERT INTO Account (account_id, name, balance_amount, balance_last_update_date, currency_id, type)
VALUES (3, 'Portefeuille Espèces', 200.00, NOW(), 1, 'ESPÈCE');

INSERT INTO Account (account_id, name, balance_amount, balance_last_update_date, currency_id, type)
VALUES (4, 'Portefeuille Mobile Money', 300.00, NOW(), 1, 'MOBILE_MONEY');

-- INSERT transactions
INSERT INTO Transaction (transaction_id, label, amount, date_time, transaction_type, account_id)
VALUES ('1', 'Dépôt Initial', 1000.00, NOW(), 'CREDIT', 1);

INSERT INTO Transaction (transaction_id, label, amount, date_time, transaction_type, account_id)
VALUES ('2', 'Dépôt Initial', 500.00, NOW(), 'CREDIT', 2);

INSERT INTO Transaction (transaction_id, label, amount, date_time, transaction_type, account_id)
VALUES ('3', 'Dépôt Initial', 200.00, NOW(), 'CREDIT', 3);

INSERT INTO Transaction (transaction_id, label, amount, date_time, transaction_type, account_id)
VALUES ('4', 'Dépôt Initial', 300.00, NOW(), 'CREDIT', 4);

INSERT INTO Transaction (transaction_id, label, amount, date_time, transaction_type, account_id)
VALUES ('5', 'Achat en ligne', 50.00, NOW(), 'DEBIT', 1);

INSERT INTO Transaction (transaction_id, label, amount, date_time, transaction_type, account_id)
VALUES ('6', 'Intérêts mensuels', 10.00, NOW(), 'CREDIT', 2);

-- CREATE currency_value
CREATE TABLE IF NOT EXISTS currency_value (
    id INT PRIMARY KEY,
    currency_from INT,
    currency_to INT,
    value DECIMAL(10, 2),
    date_value DATE,
    FOREIGN KEY (currency_from) REFERENCES currencies(currency_id),
    FOREIGN KEY (currency_to) REFERENCES currencies(currency_id)
);
