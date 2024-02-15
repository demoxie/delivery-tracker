package com.carbon.deliverytracker.entity;

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
@Table("warehouse_info")
public class WareHouseInfo extends BaseEntity {
    @Column("name")
    private String name;

    @Column("address_id")
    private Long addressId;

    @Transient
    private Address address;
}
