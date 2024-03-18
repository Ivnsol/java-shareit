DROP TABLE IF EXISTS users, items, bookings, comments;

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(512) UNIQUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS items (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL,
    owner BIGINT,
    CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES users(id)
    );

CREATE table if not exists bookings (
    id BIGSERIAL PRIMARY KEY NOT NULL,
    start TIMESTAMP,
    end_time TIMESTAMP,
    status varchar(255),
    item_id BIGINT,
    booker BIGINT,
    FOREIGN KEY (item_id) REFERENCES  items(id),
    FOREIGN KEY (booker) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id BIGSERIAL PRIMARY KEY NOT NULL,
    text VARCHAR(500),
    item_id BIGINT REFERENCES items (id),
    user_id BIGINT REFERENCES users (id),
    created TIMESTAMP WITHOUT TIME ZONE
);

