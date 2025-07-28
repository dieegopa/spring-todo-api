CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)                       NOT NULL,
    email      VARCHAR(255)                       NOT NULL,
    password   VARCHAR(255)                       NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE tasks
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    user_id        BIGINT                             NOT NULL,
    name           VARCHAR(20)                        NOT NULL,
    description    TEXT NULL,
    start_datetime DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    end_datetime   DATETIME NULL,
    completed      BOOLEAN  DEFAULT FALSE             NOT NULL,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id),
    CONSTRAINT `FK_tasks_users` FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);