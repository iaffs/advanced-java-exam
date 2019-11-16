create table member_to_project (
            id serial primary key,
            project_name varchar(100) not null,
            member_name varchar(100) not null,
            task_name varchar(100) not null,
            status_name varchar(100) default 'to do'
);