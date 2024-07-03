INSERT INTO profile ( name, surname, email, password, status, visible, create_date, role)
VALUES ('Adminjon', 'Adminov', 'muzaffarmike2@gmail.com', '827ccbeea8a706c4c34a16891f84e7b', 'ACTIVE', true, now(),
        'ROLE_ADMIN') ON CONFLICT (id) DO NOTHING;
SELECT setval('profile_id_seq', max(id))
FROM profile;
