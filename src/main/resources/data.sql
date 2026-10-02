INSERT INTO category(name) VALUES ('Eurogames');
INSERT INTO category(name) VALUES ('Ameritrash');
INSERT INTO category(name) VALUES ('Familiar');

INSERT INTO author(name, nationality) VALUES ('Alan R. Moon', 'US');
INSERT INTO author(name, nationality) VALUES ('Vital Lacerda', 'PT');
INSERT INTO author(name, nationality) VALUES ('Simone Luciani', 'IT');
INSERT INTO author(name, nationality) VALUES ('Perepau Llistosella', 'ES');
INSERT INTO author(name, nationality) VALUES ('Michael Kiesling', 'DE');
INSERT INTO author(name, nationality) VALUES ('Phil Walker-Harding', 'US');

INSERT INTO game(title, age, category_id, author_id) VALUES ('On Mars', '14', 1, 2);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Aventureros al tren', '8', 3, 1);
INSERT INTO game(title, age, category_id, author_id) VALUES ('1920: Wall Street', '12', 1, 4);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Barrage', '14', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Los viajes de Marco Polo', '12', 1, 3);
INSERT INTO game(title, age, category_id, author_id) VALUES ('Azul', '8', 3, 5);

INSERT INTO clients(name) VALUES ('Jerry');
INSERT INTO clients(name) VALUES ('Amber');
INSERT INTO clients(name) VALUES ('Alex');

INSERT INTO loan (client_id, game_id, start_date, end_date) VALUES (1, 2, '2026-01-01', '2026-01-15');
INSERT INTO loan (client_id, game_id, start_date, end_date) VALUES (2, 1, '2026-02-01', '2026-02-15');
INSERT INTO loan (client_id, game_id, start_date, end_date) VALUES (3, 4, '2026-03-01', '2026-03-15');
INSERT INTO loan (client_id, game_id, start_date, end_date) VALUES (2, 6, '2026-04-01', '2026-04-15');
INSERT INTO loan (client_id, game_id, start_date, end_date) VALUES (1, 3, '2026-05-01', '2026-05-15');
INSERT INTO loan (client_id, game_id, start_date, end_date) VALUES (3, 5, '2026-06-01', '2026-06-15');