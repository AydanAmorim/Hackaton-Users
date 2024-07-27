CREATE DATABASE securityspring;

\c securityspring;

CREATE TABLE IF NOT EXISTS tb_roles (
    id INT PRIMARY KEY,
    type VARCHAR(50)
);

WITH insert_data AS (
    SELECT 1 AS id, 'ADMIN' AS type
)
INSERT INTO tb_roles (id, type)
SELECT id, type
FROM insert_data
WHERE NOT EXISTS (SELECT 1 FROM tb_roles WHERE type = 'ADMIN');

WITH insert_data AS (
    SELECT 2 AS id, 'BASIC' AS type
)
INSERT INTO tb_roles (id, type)
SELECT id, type
FROM insert_data
WHERE NOT EXISTS (SELECT 2 FROM tb_roles WHERE type = 'BASIC');