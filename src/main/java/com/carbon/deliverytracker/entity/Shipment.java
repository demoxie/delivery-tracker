package com.carbon.deliverytracker.entity;

import com.carbon.deliverytracker.enums.DeliveryStatus;
import com.carbon.deliverytracker.enums.PackageStatus;
import com.carbon.deliverytracker.enums.ShipmentPriority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table("shipment")
public class Shipment extends BaseEntity {
    @Column("name")
    private String name;

    @Column("description")
    private String description;

    @Column("delivery_status")
    private PackageStatus status;

    @Column("shipment_type")
    private String shipmentType;

    @Column("priority")
    private ShipmentPriority priority;

    private String deliveryDateRange;

    @Column("dispatcher_id")
    private Long dispatcherId;

    @Column("order_no")
    private String orderNo;
    @Column("tracking_no")
    private String trackingNo;

    @Column("customer_id")
    private Long customerId;

    @Column("warehouse_id")
    private Long warehouseId;
}
