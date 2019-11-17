CREATE TABLE members (
    id serial primary key not null,
    name varchar(100) not null,
    email varchar(500) not null unique
);