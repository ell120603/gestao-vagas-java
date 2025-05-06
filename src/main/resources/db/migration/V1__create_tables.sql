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
    name VARCHAR(255) NOT NULL,
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
    'admin@gestaovagasapp.com', 
    '$2a$10$3Qrx0rv8qSmZ8s3RlD5qE.uSC1c9jO2RIkH5oPBMCjWk5kKJBxJMG', 
    CURRENT_TIMESTAMP
) ON CONFLICT DO NOTHING; 