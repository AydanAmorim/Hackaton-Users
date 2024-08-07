CREATE TABLE users(
    username varchar(255) not null PRIMARY KEY,
    client_id integer not null,
    password varchar(255) not null,
    role varchar(255) not null check (role in ('BASIC', 'ADMIN')),
    unique(client_id)
);