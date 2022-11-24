insert into author (id, first_name, last_name) values (1000, 'Ivo', 'Andric');
insert into author (id, first_name, last_name) values (1001, 'Marko', 'Adamovic');
insert into author (id, first_name, last_name) values (1002, 'Niko', 'Nikolic');
insert into author (id, first_name, last_name) values (1003, 'Neko', 'Neznanic');


insert into book (id, title, description, author_id) values (1000, 'Na Drini cuprija', 'Dobra knjiga', 1000);
insert into book (id, title, description, author_id) values (1001, 'Lord of the ring', 'Good book', 1001);
insert into book (id, title, description, author_id) values (1002, 'Book Book', 'Dobra knjiga', 1002);
insert into book (id, title, description, author_id) values (1003, 'Znakovi pored puta', 'knjiga', 1003);

insert into public."user" (id, first_name, last_name, email, address, password, role)
values (1000, 'Marko', 'Adamoviv', 'adam95@gmail.com', 'Veternik', '123123', 'USER');
insert into public."user" (id, first_name, last_name, email, address, password, role)
values (1001, 'Nokola', 'Namada', 'nikola@gmail.com', 'NS', '123123', 'USER');
insert into public."user" (id, first_name, last_name, email, address, password, role)
values (1002, 'Milos', 'Milosevic', 'beogradjanin@gmail.com', 'NovISad', '33333', 'USER');

insert into book_copy (id, identification, is_rented, book_id) values (1000, 'aaa111', false, 1000);
insert into book_copy (id, identification, is_rented, book_id) values (1001, 'aaa112', false, 1000);
insert into book_copy (id, identification, is_rented, book_id) values (1002, 'aaa113', false, 1001);
insert into book_copy (id, identification, is_rented, book_id) values (1003, 'aaa114', false, 1001);
insert into book_copy (id, identification, is_rented, book_id) values (1004, 'aaa115', false, 1003);
insert into book_copy (id, identification, is_rented, book_id) values (1005, 'aaa116', false, 1003);

insert into book_rental(id, book_copy_id, user_id, rent_start, rent_end)
values (1000, 1000, 1000, DATE '2015-12-17', null);
