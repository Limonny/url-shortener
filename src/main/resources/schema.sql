CREATE TABLE users(
    email VARCHAR(100) PRIMARY KEY,
    password VARCHAR(128) NOT NULL,
    role VARCHAR(5) NOT NULL,
    status VARCHAR(6) NOT NULL
);

CREATE TABLE url(
    short VARCHAR(6) PRIMARY KEY,
    original VARCHAR(2000) NOT NULL
);

CREATE UNIQUE INDEX original_idx ON url (original);