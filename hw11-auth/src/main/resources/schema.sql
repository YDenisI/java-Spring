create table authors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name varchar(255)
);

create table genres (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name varchar(255)
);

create table books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade
);

create table comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comment varchar(255),
    book_id bigint references books(id) on delete cascade
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE authorities (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    authority VARCHAR(50) NOT NULL,
    user_id bigint REFERENCES users(id) on delete cascade
);
