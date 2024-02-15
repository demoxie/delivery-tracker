-- Description: This creates the shipment table
create table if not exists activity(
                                       id bigint primary key auto_increment,
                                       action varchar(255),
                                       description varchar(255),
                                       package_status varchar(255),
                                       shipment_id bigint,
                                       action_by varchar(255),
                                       created_at timestamp default current_timestamp,
                                       updated_at timestamp default current_timestamp,
                                       foreign key (shipment_id) references shipment(id),
                                       combined_status VARCHAR(50) GENERATED ALWAYS AS (CASE WHEN package_status IN ('PICKED_UP', 'DELIVERED') THEN package_status END) STORED,
                                       UNIQUE KEY unique_picked_up_delivered (combined_status)
);