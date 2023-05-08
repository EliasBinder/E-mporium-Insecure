CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    type BOOLEAN DEFAULT FALSE,
    emergencyEmail VARCHAR(255) DEFAULT NULL,
    emergencyPhone VARCHAR(255) DEFAULT NULL,
    forgotToken VARCHAR(36) DEFAULT NULL,
    registrationToken VARCHAR(36) DEFAULT NULL,
    sessionToken VARCHAR(36) DEFAULT NULL
);
