-- Description: This script creates the address table.
create table if not exists address
(
    id      bigint primary key auto_increment not null,
    address_line1  varchar(255),
    address_line2  varchar(255),
    city    varchar(255),
    state   varchar(255),
    zip     varchar(255),
    country varchar(255),
    created_at timestamp default current_timestamp not null,
    updated_at timestamp default current_timestamp not null
);