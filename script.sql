CREATE TABLE Account (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    balance_amount DOUBLE,
    balance_last_update_date DATETIME,
    currency_id INT,
    type VARCHAR(20),
    FOREIGN KEY (currency_id) REFERENCES Currency(id)
);

CREATE TABLE Currency (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    code VARCHAR(10)
);

CREATE TABLE Transaction (
    id INT PRIMARY KEY,
    label VARCHAR(100),
    amount DOUBLE,
    date_time DATETIME,
    transaction_type VARCHAR(10),
    account_id INT,
    FOREIGN KEY (account_id) REFERENCES Account(id)
);
