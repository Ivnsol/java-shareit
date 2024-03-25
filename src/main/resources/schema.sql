DROP TABLE IF EXISTS users, items, bookings, comments, requests;

CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                                     name VARCHAR(255) NOT NULL,
                                     email VARCHAR(512) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS requests (
                                        request_id   BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                                        description  VARCHAR(1000) NOT NULL,
                                        requester_id BIGINT REFERENCES users (id) ON DELETE CASCADE,
                                        created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                        CONSTRAINT pk_request PRIMARY KEY (request_id)
);

CREATE TABLE IF NOT EXISTS items (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                                     name VARCHAR(255) NOT NULL,
                                     description VARCHAR(255) NOT NULL,
                                     available BOOLEAN NOT NULL,
                                     owner BIGINT,
                                     request_id         BIGINT REFERENCES requests (request_id) ON DELETE CASCADE,
                                     CONSTRAINT fk_owner FOREIGN KEY (owner) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS bookings (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                                        start TIMESTAMP,
                                        end_time TIMESTAMP,
                                        status VARCHAR(255),
                                        item_id BIGINT,
                                        booker BIGINT,
                                        FOREIGN KEY (item_id) REFERENCES items(id),
                                        FOREIGN KEY (booker) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
                                        text VARCHAR(500),
                                        item_id BIGINT,
                                        user_id BIGINT,
                                        created TIMESTAMP,
                                        FOREIGN KEY (item_id) REFERENCES items(id),
                                        FOREIGN KEY (user_id) REFERENCES users(id)
);

