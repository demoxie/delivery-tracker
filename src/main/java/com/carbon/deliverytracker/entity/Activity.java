package com.carbon.deliverytracker.entity;

import com.carbon.deliverytracker.enums.PackageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table("activity")
public class Activity extends BaseEntity{
    @Column("action")
    private String action;
    @Column("description")
    private String description;
    @Column("package_status")
    private PackageStatus packageStatus;
    @Column("shipment_id")
    private Long shipmentId;
    private String actionBy;
}
