package com.carbon.deliverytracker.entity;

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
@Table("address")
public class Address extends BaseEntity{
    @Column("address_line1")
    private String addressLine1;
    @Column("address_line2")
    private String addressLine2;
    @Column("city")
    private String city;
    @Column("state")
    private String state;
    @Column("zip")
    private String zip;
    @Column("country")
    private String country;
}
