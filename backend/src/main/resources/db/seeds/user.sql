INSERT INTO t_user (id, first_name, middle_name, last_name, "e_mail", password, user_role, birthdate, created, updated)
VALUES ('a085cc2d-21a5-4515-9ce5-9d02bb8a02c6', 'test', null, 'user', 'testuser@gmail.com', '$2a$10$IVLBCJ8ed8zh1aYeui6Nwu4uauH/Uwtrdkd5PshFdCP9Yo0U2ltjK', 'ADMIN', null, NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO t_school (id, name, address, city, postcode, created, updated)
VALUES ('736c2e92-6817-4d47-8f8f-1a4c42edfc73', 'Benedict', 'Vulkanstrasse 106', 'Zürich', '8048', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO t_grade (id, name, school_id, created, updated)
VALUES ('a2452b33-6ff3-4c0d-833d-9ae2af5a9d45', 'First Grade A', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW()),
       ('9d7bb429-5708-4833-8358-d29c4a8c1db3', 'First Grade B', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW()),
       ('ec61f7b5-1240-463d-8a5a-2d2ef60e19b6', 'First Grade C', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW()),
       ('f3f36f26-bd70-45a7-baf8-f4c588a25e1d', 'Second Grade A', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW()),
       ('40c5e0a1-bbcf-4e14-b5c1-9a3df0488f2d', 'Second Grade B', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW()),
       ('ae3fc429-88f5-4d60-bc7c-6c1fdd892d10', 'Second Grade C', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW()),
       ('6786ac30-ba43-4319-b3e8-2c21c4ecac7e', 'Third Grade A', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW()),
       ('b097032e-4e1b-4e1e-b640-617e96f6ee16', 'Third Grade B', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW()),
       ('e9bc1bfa-90ae-4971-99f2-ff7b7d516758', 'Third Grade C', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW()),
       ('69e2a40c-b2cb-4a8f-9aaf-23f9e9d477c5', 'Fourth Grade A', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW()),
       ('b740042b-af48-4968-8304-d786fcf25477', 'Fourth Grade B', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW()),
       ('eff58eb7-6214-468f-b7b4-2894d897dffb', 'Fourth Grade C', '736c2e92-6817-4d47-8f8f-1a4c42edfc73', NOW(), NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO t_user_school (user_id, school_id)
VALUES ('a085cc2d-21a5-4515-9ce5-9d02bb8a02c6', '736c2e92-6817-4d47-8f8f-1a4c42edfc73'),
ON CONFLICT (user_id, school_id) DO NOTHING;

INSERT INTO t_user_grade (user_id, grade_id)
VALUES ('a085cc2d-21a5-4515-9ce5-9d02bb8a02c6', 'a2452b33-6ff3-4c0d-833d-9ae2af5a9d45'),
       ('a085cc2d-21a5-4515-9ce5-9d02bb8a02c6', '40c5e0a1-bbcf-4e14-b5c1-9a3df0488f2d'),
ON CONFLICT (user_id, grade_id) DO NOTHING;