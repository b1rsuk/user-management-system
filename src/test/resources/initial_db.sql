CREATE TABLE IF NOT EXISTS users
(
    id           BIGSERIAL
    PRIMARY KEY,
    address      VARCHAR(86),
    birth_date   DATE,
    email        VARCHAR(255),
    first_name   VARCHAR(100),
    last_name    VARCHAR(100),
    phone_number VARCHAR(255)
);

