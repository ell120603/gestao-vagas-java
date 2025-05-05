CREATE TABLE IF NOT EXISTS candidates (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS company (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    website VARCHAR(255),
    description TEXT,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS admin_users (
    id UUID PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);

INSERT INTO admin_users (id, username, email, password, created_at)
VALUES (
    '550e8400-e29b-41d4-a716-446655440000', 
    'admin', 
    'admin@gestaovaga.com', 
    '$2a$12$MRp/aMjxxba6X1ETJMmee.d/BYRLhRv38g7adG2ceVOE5SIOiTrTC', 
    CURRENT_TIMESTAMP
) ON CONFLICT DO NOTHING; 