package com.carbon.deliverytracker.exception;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
public class APIException extends RuntimeException{
    private final String message;
    private final String path;
    private final String status;
    private final String method;
    private final String error;
}
