-- Description: This file is used to create the table for the dispatcher
create table if not exists dispatcher(
     id bigint primary key auto_increment,
     first_name varchar(100),
     last_name varchar(100),
     email varchar(100),
     phone varchar(100),
     dispatcher_no varchar(100),
     created_at timestamp default current_timestamp,
     updated_at timestamp default current_timestamp on update current_timestamp
);