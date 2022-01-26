CREATE TABLE url(
    short VARCHAR(6) PRIMARY KEY,
    original VARCHAR(2000) NOT NULL
);

CREATE UNIQUE INDEX original_idx ON url (original);