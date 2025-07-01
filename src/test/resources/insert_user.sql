INSERT INTO users (id, created_at, updated_at, username, role, email, password, profile_id)
VALUES ('00000000-0000-0000-0000-000000000001',
        NOW(),
        NOW(),
        'user1',
        'USER',
        'user1@example.com',
        'encrypted-password',
        null);

INSERT INTO users (id, created_at, updated_at, username, role, email, password, profile_id)
VALUES ('00000000-0000-0000-0000-000000000002',
        NOW(),
        NOW(),
        'user2',
        'USER',
        'user2@example.com',
        'encrypted-password',
        null);
