-- Description : This creates the shipment table
create table shipment (
                         id bigint primary key auto_increment,
                         name varchar(255),
                         description varchar(255),
                         delivery_status varchar(50),
                         shipment_type varchar(50),
                         priority varchar(50),
                         delivery_date_range varchar(255),
                         order_no varchar(50),
                         tracking_no varchar(50),
                         dispatcher_id bigint not null ,
                         customer_id bigint not null,
                         warehouse_id bigint not null,
                            created_at timestamp default current_timestamp,
                            updated_at timestamp default current_timestamp,
                         combined_status VARCHAR(50) GENERATED ALWAYS AS (CASE WHEN delivery_status IN ('PICKED_UP', 'DELIVERED') THEN delivery_status END) STORED,
                         foreign key (dispatcher_id) references dispatcher(id),
                         foreign key (customer_id) references customer_info(id),
                         foreign key (warehouse_id) references warehouse_info(id),
                         UNIQUE KEY unique_picked_up_delivered (combined_status)
);
