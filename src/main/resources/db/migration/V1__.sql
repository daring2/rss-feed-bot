CREATE SEQUENCE subscription_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE subscription
(
    id       BIGINT       NOT NULL,
    chat_id  BIGINT       NOT NULL,
    feed_url VARCHAR(255) NOT NULL,
    CONSTRAINT pk_subscription PRIMARY KEY (id)
);

ALTER TABLE subscription
    ADD CONSTRAINT uc_subscription_chat_id UNIQUE (chat_id, feed_url);