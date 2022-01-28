CREATE TABLE users(
    email VARCHAR(100) PRIMARY KEY,
    password VARCHAR(128) NOT NULL,
    role VARCHAR(5) NOT NULL,
    status VARCHAR(6) NOT NULL
);

CREATE TABLE url(
    short VARCHAR(6) PRIMARY KEY,
    original VARCHAR(2000) NOT NULL,
    expire DATE NOT NULL
);

CREATE UNIQUE INDEX original_idx ON url (original);

CREATE TABLE visitor(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(6) NOT NULL,
    ip_address VARCHAR(45) NOT NULL,
    FOREIGN KEY (url) REFERENCES url(short) ON DELETE CASCADE
);