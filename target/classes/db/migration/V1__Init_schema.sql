
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    "dateTimeOfCreated" TIMESTAMP,
    "isActive" BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role)
);

CREATE TABLE IF NOT EXISTS token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(512) NOT NULL,
    token_type VARCHAR(50),
    expired BOOLEAN NOT NULL DEFAULT FALSE,
    revoked BOOLEAN NOT NULL DEFAULT FALSE,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_token_user_id ON token (user_id);

CREATE TABLE IF NOT EXISTS cards (
    id BIGSERIAL PRIMARY KEY,
    card_number_encrypted VARCHAR(512) NOT NULL,
    card_number_last_four VARCHAR(4) NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    expiration_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    balance NUMERIC(19, 2) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_cards_card_number_encrypted ON cards (card_number_encrypted);
CREATE INDEX IF NOT EXISTS idx_cards_user_id ON cards (user_id);

