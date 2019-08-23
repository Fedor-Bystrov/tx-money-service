CREATE TABLE accounts
(
    account_id INT AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE transactions
(
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    creation_time  TIMESTAMP      NOT NULL DEFAULT now(),
    amount         NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
    recipient      INT REFERENCES accounts (account_id),
    sender         INT REFERENCES accounts (account_id)
);

INSERT INTO accounts
VALUES (1), -- account(1) is admin account
       (2),
       (3),
       (4),
       (5),
       (6);

--
-- Balances after initialization:
---------------------------------
-- account 1: 1_000_000.00 coins
-- account 2: 500_000.00 coins
-- account 3: 500_000.00 coins
-- account 4: 474_999.75 coins
-- account 5: 25_000.25 coins
-- account 6: 0.00 coins
--
INSERT INTO transactions (creation_time, amount, recipient, sender)
VALUES ('2019-08-13 00:00:00', 2500000.00, 1, 1), -- initializing account(1) with 2_500_000.00
       ('2019-08-13 01:01:00', 500000.00, 2, 1),  -- account(1) sent 500_000.00 to account(2)
       ('2019-08-13 02:02:00', 500000.00, 3, 1),  -- account(1) sent 500_000.00 to account(3)
       ('2019-08-13 03:03:00', 500000.00, 4, 1),  -- account(1) sent 500_000.00 to account(4)
       ('2019-08-13 04:04:00', 25000.25, 5, 4);   -- account(4) sent 25_000.25  to account(5)
