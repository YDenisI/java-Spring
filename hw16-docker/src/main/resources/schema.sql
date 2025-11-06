create table authors (
    id SERIAL PRIMARY KEY,
    full_name varchar(255)
);

create table genres (
    id SERIAL PRIMARY KEY,
    name varchar(255)
);

create table books (
    id SERIAL PRIMARY KEY,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade
);

create table comments (
    id SERIAL PRIMARY KEY,
    comment varchar(255),
    book_id bigint references books(id) on delete cascade
);
