insert into author
values(default, 'Ivo', 'Andric');
insert into author
values(default, 'Marko', 'Adamovic');
insert into author
values(default, 'Niko', 'Nikolic');
insert into author
values(default, 'Neko', 'Neznanic');


insert into book
values (default, 'Na Drini cuprija', 'Dobra knjiga', '1');
insert into book
values (default, 'Lord of the ring', 'Good book', '2');
insert into book
values (default, 'Book Book', 'Dobra knjiga', '3');
insert into book
values(default, 'Znakovi pored puta', 'knjiga', '1');

insert into public."user"
values(default, 'Veternik', 'adam95@gmail.com', 'Marko', 'Adamoviv', '$2a$12$pweqL4C1wT74yNuJRLknheBXbzhJcbUbz.sTHtqEnUerEY6n8Xsui', 'ADMINISTRATOR');
insert into public."user"
values(default, 'NS', 'nikola@gmail.com', 'Nokola', 'Namada', '$2a$12$pweqL4C1wT74yNuJRLknheBXbzhJcbUbz.sTHtqEnUerEY6n8Xsui', 'USER');
insert into public."user"
values(default, 'BG', 'beogradjanin@gmail.com', 'Milos', 'Milosevic', '$2a$12$pweqL4C1wT74yNuJRLknheBXbzhJcbUbz.sTHtqEnUerEY6n8Xsui', 'ADMINISTRATOR');

insert into book_copy
values (default, 'aaa111', false, '1');
insert into book_copy
values (default, 'aaa112', false, '1');
insert into book_copy
values (default, 'aaa113', false, '1');
insert into book_copy
values (default, 'aaa114', false, '1');
insert into book_copy
values (default, 'aaa115', false, '2');
insert into book_copy
values (default, 'aaa116', false, '2');
