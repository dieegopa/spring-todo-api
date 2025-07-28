CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)                        NOT NULL,
    email      VARCHAR(255)                        NOT NULL,
    password   VARCHAR(255)                        NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);