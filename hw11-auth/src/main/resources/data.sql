insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into comments(comment, book_id)
values ('Comment_1', 1), ('Comment_2', 2), ('Comment_3', 1);

INSERT INTO users (username, password, enabled) VALUES
('user', '$2a$12$.2hnc0msv8c9yPEvfF6JK.sDAPtoQkj2uYH6iZRC/HSFhQ3UDaGgS', TRUE),
('admin', '$2a$12$.2hnc0msv8c9yPEvfF6JK.sDAPtoQkj2uYH6iZRC/HSFhQ3UDaGgS', TRUE);

INSERT INTO authorities (username, authority) VALUES
('user', 'ROLE_USER'),
('admin', 'ROLE_ADMIN');