-- Description : This file is used to create the customer_info table in the database
create table if not exists customer_info(
    id bigint primary key auto_increment,
    first_name varchar(100),
    last_name varchar(100),
    email varchar(100),
    phone varchar(100),
    address_id bigint not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp on update current_timestamp,
    foreign key (address_id) references address(id)
);