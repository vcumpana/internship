insert into credentials(password, role, status, username)values ('!2Dsfsdfdf', 'ROLE_COMPANY', 'ACCEPTED', 'orangesrl'),('!2Dsfsdfdf', 'ROLE_COMPANY', 'ACCEPTED', 'moldcell'),('!2Dsfsdfdf', 'ROLE_COMPANY', 'ACCEPTED', 'moldtelecom'),('!2Dsfsdfdf', 'ROLE_COMPANY', 'ACCEPTED', 'andyzzp'),('!2Dsfsdfdf', 'ROLE_COMPANY', 'ACCEPTED', 'airmoldova'),('!2Dsfsdfdf', 'ROLE_COMPANY', 'ACCEPTED', 'leogrand'),('$2a$04$92omvzxlVIW/OwtiyBozQ.VFIfjFmD6CCFnEAomKqFP.QLKJbN5lq', 'ROLE_USER', 'ACCEPTED', 'vcumpana'),('$2a$04$92omvzxlVIW/OwtiyBozQ.VFIfjFmD6CCFnEAomKqFP.QLKJbN5lq', 'ROLE_USER', 'ACCEPTED', 'iocojocari'),('$2a$04$92omvzxlVIW/OwtiyBozQ.VFIfjFmD6CCFnEAomKqFP.QLKJbN5lq', 'ROLE_USER', 'ACCEPTED', 'rsagivaliev');
insert into users (name, surname, credential_id) values ('Victor', 'Cumpana', 7),('Ion', 'Cojocari', 8),('Rustam', 'Shagivaliev', 9);
insert into company (address, bank_account, email, name, credential_id) values ('', '', '', 'Orange SRL', 2),('', '', '', 'Moldcell SA', 3),('', '', '', 'Moldtelecom SA', 4),('', '', '', 'Anddys Pizza', 5),('', '', '', 'AirMoldova SA', 6),('', '', '', 'Leogrand Hotel SA', 7);
insert into categories(name) values('GSM');
insert into categories(name) values('INTERNET');
insert into categories(name) values('LOANS');
insert into categories(name) values('CATERING');
insert into categories(name) values('BOOKING');
insert into categories(name) values('AIRTICKETS');
insert into service (description, price, title, category_id) values('mobile telephony', 10, 'telephony', 1),('internet broadband', 70, 'FiberLink', 2),('Monney short-time loans ', 234, 'Microcredit', 3),('daily meals', 30, 'Meals', 4),('hotel booking', 12, 'Hotel Boking', 5),('flytickets booking', 89, 'FlyTickets', 6),('sms service', 100, 'sms', 1);
insert into service (description, price, title, category_id) values('mobile telephony', 10, 'telephony', 1),('internet broadband', 70, 'FiberLink', 2),('Monney short-time loans ', 234, 'Microcredit', 3),('daily meals', 30, 'Meals', 4),('hotel booking', 12, 'Hotel Boking', 5),('flytickets booking', 89, 'FlyTickets', 6),('sms service', 100, 'sms', 1);
insert into service (description, price, title, category_id) values('mobile telephony', 10, 'telephony', 1),('internet broadband', 70, 'FiberLink', 2),('Monney short-time loans ', 234, 'Microcredit', 3),('daily meals', 30, 'Meals', 4),('hotel booking', 12, 'Hotel Boking', 5),('flytickets booking', 89, 'FlyTickets', 6),('sms service', 100, 'sms', 1);
insert into service (description, price, title, category_id) values('mobile telephony', 10, 'telephony', 1),('internet broadband', 70, 'FiberLink', 2),('Monney short-time loans ', 234, 'Microcredit', 3),('daily meals', 30, 'Meals', 4),('hotel booking', 12, 'Hotel Boking', 5),('flytickets booking', 89, 'FlyTickets', 6),('sms service', 100, 'sms', 1);
insert into company_services(company_id, services_id) values (1,1),(1,7), (2,3),(3,2),(4,4), (5,6),(6,5);
insert into company_services(company_id, services_id) values (1,8),(1,14), (2,10),(3,9),(4,11), (5,13),(6,12);
insert into company_services(company_id, services_id) values (1,15),(1,21), (2,17),(3,16),(4,18), (5,20),(6,19);
insert into company_services(company_id, services_id) values (1,22),(1,28), (2,24),(3,23),(4,25), (5,27),(6,26);
INSERT INTO credentials(username,password,status,role) VALUES ('login_user','$2a$10$O2M/QcKwMyboENRAtMvEXebcyWkwO.yCbTb6orzCKROvS8lFqVVK2','ACCEPTED','ROLE_USER');
INSERT INTO users(name, surname, email, credential_id) VALUES ('name', 'surname','user@mail.ru', 10);
INSERT INTO credentials(username,password,status,role) VALUES ('admin','$2a$10$O2M/QcKwMyboENRAtMvEXebcyWkwO.yCbTb6orzCKROvS8lFqVVK2','ACCEPTED','ROLE_ADMIN');
INSERT INTO admins(credential_id) VALUES ( 11);