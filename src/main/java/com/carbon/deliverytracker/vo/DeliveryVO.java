package com.carbon.deliverytracker.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeliveryVO {
    private String name;
    private String description;
    private String shipmentType;
    private String deliveryDateRange;
    private DispatcherVO dispatcherVO;
    private String orderNo;
    private String trackingNo;
    private CustomerInfoVO customerInfoVO;
    private WareHouseInfoVO wareHouseInfoVO;
    private String status;
    private String priority;
    private String deliveryStatus;
    private Long id;
    private ActivityVO activityVO;
    private String createdAt;
    private String updatedAt;
}
