package com.carbon.deliverytracker.dto;

import com.carbon.deliverytracker.enums.PackageStatus;
import com.carbon.deliverytracker.enums.ShipmentPriority;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryDto {
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("shipmentType")
    private String shipmentType;
    @JsonProperty("status")
    private PackageStatus status;
    @JsonProperty("deliveryDateRange")
    private String deliveryDateRange;
    @JsonProperty("dispatcherDto")
    private DispatcherDto dispatcherDto;
    @JsonProperty("orderNo")
    private String orderNo;
    @JsonProperty("priority")
    private ShipmentPriority priority;
    @JsonProperty("trackingNo")
    private String trackingNo;
    @JsonProperty("customerDto")
    private CustomerInfoDto customerDto;
    @JsonProperty("wareHouseDto")
    private WareHouseInfoDto wareHouseDto;
}
