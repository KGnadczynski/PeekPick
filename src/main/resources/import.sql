drop table if exists Company;
drop table if exists User;

insert into user_role(id, name) values (1,'ROLE_ADMIN');
insert into user_role(id, name) values (2,'ROLE_BUSINESS_USER');

insert into user (id,email,name,password,phoneNumber,status,company_id, createDate)
  VALUES(1,'admin@admin.com', 'admin','$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.', '123123', 'ACTIVE', NULL, now());

insert into user_user_role(user_id, role_id) values (1,1);

insert into Business_plan(id, nettValue, grossValue, messageCount, status) values (1000,5.0, 6.15, 5, 'ACTIVE');
insert into Business_plan(id, nettValue, grossValue, messageCount, status) values (2000,10.0, 12.3, 12, 'ACTIVE');
insert into Business_plan(id, nettValue, grossValue, messageCount, status) values (3000,20.0, 24.6, 25, 'ACTIVE');
insert into Business_plan(id, nettValue, grossValue, messageCount, status) values (4000,35.0, 43.05, 50, 'ACTIVE');
insert into Business_plan(id, nettValue, grossValue, messageCount, status) values (5000,60.0, 73.8, 100, 'ACTIVE');
insert into Business_plan(id, nettValue, grossValue, messageCount, status) values (6000,80.0, 98.4, 150, 'ACTIVE');

INSERT INTO company_category
(id, name, parentCategory_id) VALUES
  (1, "Kultura i rozrywka", null),
  (2, "Gastronomia i Nocne Życie", null),
  (3, "Zakupy Małe", null),
  (4, "Zakupy Duże", null),
  (5, "Usługi dla Ciała", null),
  (6, "Usułgi Inne", null),
  (7, "Sport i Turystyka", null);

INSERT INTO company_category
(id, name, parentCategory_id) VALUES
  (8, "Artyści, zespoły", 1),
  (9, "Escape roomy, parki rozrywki", 1),
  (10, "Kino, teatr", 1),
  (11, "Muzeum, wystawy", 1),
  (12, "Inne", 1);

INSERT INTO company_category
(id, name, parentCategory_id) VALUES
  (13, "Food truck", 2),
  (14, "Kawiarnie", 2),
  (15, "Kluby", 2),
  (16, "Puby", 2),
  (17, "Restauracje", 2),
  (18, "Inne", 2);

INSERT INTO company_category
(id, name, parentCategory_id) VALUES
  (19, "Księgarnia, pamiątki, upominki", 3),
  (20, "Odzież i obuwie", 3),
  (21, "Sklepy sportowe", 3),
  (22, "Sklepy spożywcze", 3),
  (23, "Inne", 3);

INSERT INTO company_category
(id, name, parentCategory_id) VALUES
  (24, "AGD, RTV", 4),
  (25, "Budowlane, ogrodnicze", 4),
  (26, "Meble, oświetlenie, akcesoria", 4),
  (27, "Salony samochodowe", 4),
  (28, "Inne", 4);

INSERT INTO company_category
(id, name, parentCategory_id) VALUES
  (29, "Dietetycy i zdrowa żywność", 5),
  (30, "Gabinety lekarskie", 5),
  (31, "Salony masażu, spa, solarium", 5),
  (32, "Salony tatuażu", 5),
  (33, "Salony fryzjerskie i kosmetyczne", 5),
  (34, "Inne", 5);

INSERT INTO company_category
(id, name, parentCategory_id) VALUES
  (35, "Budowlane", 6),
  (36, "Edukacyjne", 6),
  (37, "Finansowe", 6),
  (38, "Fotograficzne, komputerowe", 6),
  (39, "Mechanicy, wulkanizacja, stacje paliw", 6),
  (40, "Inne", 6);

INSERT INTO company_category
(id, name, parentCategory_id) VALUES
  (41, "Baseny i parki wodne", 7),
  (42, "Hotele i Pensjonaty", 7),
  (43, "Kluby sportowe", 7),
  (44, "Kursy, szkolenia", 7),
  (45, "Siłownia i fitness", 7),
  (46, "Sporty ekstremalne", 7),
  (47, "Sporty towarzyskie", 7),
  (48, "Inne", 7);