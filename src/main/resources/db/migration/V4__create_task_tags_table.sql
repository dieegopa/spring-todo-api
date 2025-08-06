CREATE TABLE task_tags
(
    id         BIGINT AUTO_INCREMENT              NOT NULL,
    task_id    BIGINT                             NOT NULL,
    tag_id     BIGINT                             NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT `PRIMARY_TASK_TAGS` PRIMARY KEY (id),
    CONSTRAINT `FK_task_tags_tasks` FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    CONSTRAINT `FK_task_tags_tags` FOREIGN KEY (tag_id) REFERENCES tags (id) ON DELETE CASCADE
);