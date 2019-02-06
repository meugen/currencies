CREATE TABLE currencies (
  id INTEGER NOT NULL,
  name VARCHAR(100) NOT NULL,
  code VARCHAR(10) NOT NULL,
  PRIMARY KEY (id));
CREATE INDEX idx_currencies_name ON currencies (name);

CREATE TABLE exchanges (
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  currency_id INTEGER NOT NULL,
  exchange_date TIMESTAMP NOT NULL,
  exchange_rate NUMERIC NOT NULL,
  FOREIGN KEY (currency_id) REFERENCES currencies (id));
CREATE INDEX idx_exchanges_date ON exchanges (exchange_date);