insert into user(id, name, email, password, status) values (1,'Roy','test@user.com','spring', 'ACTIVE');
insert into user(id, name, email, password, status) values (2,'Craig','test@user2.com','spring', 'ACTIVE');
insert into user(id, name, email, password, status) values (3,'Greg','test@user3.com','spring', 'NON_ACTIVE');
 
insert into role(id, name) values (1,'ROLE_USER');
insert into role(id, name) values (2,'ROLE_ADMIN');
insert into role(id, name) values (3,'ROLE_GUEST'); 

insert into user_role(user_id, role_id) values (1,1);
insert into user_role(user_id, role_id) values (1,2);
insert into user_role(user_id, role_id) values (2,1);
insert into user_role(user_id, role_id) values (3,1);

INSERT INTO companycategory
(id, name, parentCategory_id) VALUES
  (1, "Kultura i rozrywka", null),
  (2, "Gastronomia i Nocne Życie", null),
  (3, "Zakupy Małe", null),
  (4, "Zakupy Duże", null),
  (5, "Usługi dla Ciała", null),
  (6, "Usułgi inne", null),
  (7, "Sport i Turystyka", null);

INSERT INTO companycategory
(id, name, parentCategory_id) VALUES
  (8, "Artyści, zespoły", 1),
  (9, "Escape roomy, parki rozrywki", 1),
  (10, "Kino, teatr", 1),
  (11, "Muzeum, wystawy", 1),
  (12, "Inne", 1);

INSERT INTO companycategory
(id, name, parentCategory_id) VALUES
  (13, "Food truck", 2),
  (14, "Kawiarnie", 2),
  (15, "Kluby", 2),
  (16, "Puby", 2),
  (17, "Restauracje", 2),
  (18, "Inne", 2);
