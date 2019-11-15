create table status (
    id serial primary key,
    name varchar(100) default 'to do'
);

insert into status values (1,'to do');
insert into status values (2,'ongoing');
insert into status values (3,'done');