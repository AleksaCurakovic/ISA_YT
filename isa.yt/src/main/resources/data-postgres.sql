INSERT INTO ROLE (name) VALUES ('ROLE_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO USERS (last_password_reset_date, address, email, username, first_name, last_name, password, enabled)
VALUES (
    '2026-01-10 21:29:58.27',
    'Cajkovskog 7',
    'aleksa.curakovic@gmail.com',
    'aleksa02',
    'Aleksa',
    'Curakovic',
    '$2a$10$qZlVZ/6ERYsjsImXjP/Jce4LSjR8Y2LV6JpOGM6dR9tJWdUOYRiE.',
    true
),(
    '2026-01-10 20:29:58.27',
    'Cajkovskog 8',
    'milos.milosevic@gmail.com',
    'Milos02',
    'Milos',
    'Milosevic',
    '$2a$10$qZlVZ/6ERYsjsImXjP/Jce4LSjR8Y2LV6JpOGM6dR9tJWdUOYRiE.',
    true
),(
    '2026-01-10 21:11:58.27',
    'Cajkovskog 3',
    'nikola.nikolevic@gmail.com',
    'nikola02',
    'Nikola',
    'Nikic',
    '$2a$10$qZlVZ/6ERYsjsImXjP/Jce4LSjR8Y2LV6JpOGM6dR9tJWdUOYRiE.',
    true
),(
    '2026-01-10 22:29:58.27',
    'Cajkovskog 8',
    'jana.janosevic@gmail.com',
    'jana02',
    'Jana',
    'Janosevic',
    '$2a$10$qZlVZ/6ERYsjsImXjP/Jce4LSjR8Y2LV6JpOGM6dR9tJWdUOYRiE.',
    true
),(
    '2025-01-10 22:29:58.27',
    'Ulica 8',
    'darko.darkovic@gmail.com',
    'darko02',
    'Darko',
    'Darkovic',
    '$2a$10$qZlVZ/6ERYsjsImXjP/Jce4LSjR8Y2LV6JpOGM6dR9tJWdUOYRiE.',
    true
);
INSERT INTO VIDEO_UPLOADS (created_at, author, title, description, geo_Location, tags, thumbnail_url, video_url, views, likes, duration)
VALUES (
    '2026-01-10 21:36:34.935',
    'aleksa02',
    'My First Video...',
    'My First Video...',
    'Novi Sad, Serbia',
    'cool, lame, thrilling',
    '/uploads/thumbnails/50078467-bc58-4dcd-91c9-ac83fd4eb099.jpg',
    '/uploads/videos/0153c302-5e78-477a-81f6-bc999a2bbd9e.mp4',
    0,
    0,
    17
);

INSERT INTO USER_ROLE VALUES 
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5);

INSERT INTO COMMENTS (content, author_username, video_upload_id, created_at) VALUES
('Great explanation!', 'milos', 1, NOW()),
('Loved the editing 🔥', 'jana', 1, NOW()),
('Hype!', 'Aleksa02', 1, NOW()),
('USA!!!', 'nikola', 2, NOW()),
('Nice video', 'marko', 2, NOW()),
('Waiting for part 2!', 'darko', 2, NOW()),
(':)', 'nikola', 3, NOW()),
('LMAO', 'Aleksa02', 3, NOW()),
('Waiting for part 2!', 'milos', 4, NOW());
