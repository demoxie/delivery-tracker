package com.carbon.deliverytracker.exception;

import com.carbon.deliverytracker.response.APIResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIError extends APIResponse {
    private String error;
    private final String timestamp = LocalDateTime.now().toString().replace("T", " ").substring(0, 19);
    private String path;
    private HttpMethod method;
}
