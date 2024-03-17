INSERT INTO t_user (id, first_name, middle_name, last_name, "e_mail", password, user_role, birthdate, created, updated)
VALUES ('a085cc2d-21a5-4515-9ce5-9d02bb8a02c6', 'test', null, 'user', 'testuser@gmail.com', '$2a$10$IVLBCJ8ed8zh1aYeui6Nwu4uauH/Uwtrdkd5PshFdCP9Yo0U2ltjK', 'ADMIN', null, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;