package com.carbon.deliverytracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerInfoDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private AddressDto addressDto;
}
