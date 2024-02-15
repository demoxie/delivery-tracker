package com.carbon.deliverytracker.vo;

import com.carbon.deliverytracker.enums.PackageStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityVO {
    private Long id;
    private String action;
    private String description;
    private PackageStatus packageStatus;
    private Long shipmentId;
    private String actionBy;
    private String createdAt;
    private String updatedAt;
}
