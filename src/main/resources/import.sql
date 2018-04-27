INSERT INTO credentials(username,password,status,role) VALUES ('login_user','$2a$10$O2M/QcKwMyboENRAtMvEXebcyWkwO.yCbTb6orzCKROvS8lFqVVK2','ACCEPTED','ROLE_USER');
INSERT INTO users(name, surname, email, credential_id) VALUES ('name', 'surname','user@mail.ru', 1);
INSERT INTO credentials(username,password,status,role) VALUES ('admin','$2a$10$O2M/QcKwMyboENRAtMvEXebcyWkwO.yCbTb6orzCKROvS8lFqVVK2','ACCEPTED','ROLE_ADMIN');
INSERT INTO admins(credential_id) VALUES ( 2);