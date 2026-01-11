INSERT INTO ROLE (name) VALUES ('ROLE_USER');
INSERT INTO ROLE (name) VALUES ('ROLE_ADMIN');
INSERT INTO USERS (last_password_reset_date, address, email, username, first_name, last_name, password, enabled)
VALUES (
    '2026-01-10 21:29:58.27',
    'Cajkovskog 7',
    'aleksa.curakovic@gmail.com',
    'Aleksa02',
    'Aleksa',
    'Curakovic',
    '$2a$10$qZlVZ/6ERYsjsImXjP/Jce4LSjR8Y2LV6JpOGM6dR9tJWdUOYRiE.',
    true
);
INSERT INTO VIDEO_UPLOADS (created_at, author, title, description, geo_Location, tags, thumbnail_url, video_url, views, likes, duration)
VALUES (
    '2026-01-10 21:36:34.935',
    'Aleksa02',
    'My First Video...',
    'My First Video...',
    'Novi Sad, Serbia',
    'cool, lame, thrilling',
    '/uploads/thumbnails/50078467-bc58-4dcd-91c9-ac83fd4eb099.jpg',
    '/uploads/videos/0153c302-5e78-477a-81f6-bc999a2bbd9e.mp4',
    492,
    100,
    17
);
