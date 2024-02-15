-- Description: Create the warehouse_info table
create table if not exists warehouse_info
(
    id         bigint primary key auto_increment,
    name       varchar(255) not null,
    address_id    bigint       not null,
    created_at timestamp default current_timestamp,
    updated_at timestamp default current_timestamp,
    constraint warehouse_info_address_id_fk
        foreign key (address_id) references address (id)
);