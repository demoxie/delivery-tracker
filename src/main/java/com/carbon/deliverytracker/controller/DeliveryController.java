package com.carbon.deliverytracker.controller;

import com.carbon.deliverytracker.dto.DeliveryDto;
import com.carbon.deliverytracker.response.APIResponse;
import com.carbon.deliverytracker.service.DeliveryService;
import com.carbon.deliverytracker.utils.logging.LoggingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static com.carbon.deliverytracker.constant.BaseRoutes.DELIVERIES;

@RestController
@RequestMapping(DELIVERIES)
@RequiredArgsConstructor
@Validated
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping("/create")
    public Mono<APIResponse> createDelivery(@Valid @RequestBody DeliveryDto deliveryDto) {
        LoggingUtils.logRequest(deliveryDto, null, null, DELIVERIES, "POST");
        return deliveryService.createDelivery(deliveryDto)
                .map(d-> APIResponse.builder()
                        .message("Delivery created successfully")
                        .status("200")
                        .data(d)
                        .build());
    }

    @PutMapping("/{id}")
    public Mono<APIResponse> updateDelivery(@PathVariable long id, @Valid @RequestBody DeliveryDto deliveryDto) {
        LoggingUtils.logRequest(deliveryDto, null, null, DELIVERIES, "PUT");
        return deliveryService.updateDelivery(id, deliveryDto)
                .map(d-> APIResponse.builder()
                        .message("Delivery updated successfully")
                        .status("200")
                        .data(d)
                        .build());
    }

    @PutMapping("/{id}/status")
    public Mono<APIResponse> updateDeliveryStatus(@PathVariable long id, @Valid @RequestBody DeliveryDto deliveryDto) {
        LoggingUtils.logRequest(deliveryDto, null, null, DELIVERIES, "PUT");
        return deliveryService.updateDeliveryStatus(id, deliveryDto)
                .map(d-> APIResponse.builder()
                        .message("Delivery updated successfully")
                        .status("200")
                        .data(d)
                        .build());
    }

    @GetMapping("/get/{id}")
    public Mono<APIResponse> getDelivery(@PathVariable long id) {
        LoggingUtils.logRequest(null, null, null, DELIVERIES, "GET");
        return deliveryService.getDelivery(id)
                .map(d-> APIResponse.builder()
                        .message("Delivery retrieved successfully")
                        .status("200")
                        .data(d)
                        .build());
    }

    @GetMapping("/get-all")
    public Mono<APIResponse> getAllDeliveries() {
        LoggingUtils.logRequest(null, null, null, DELIVERIES, "GET");
        return deliveryService.getAllDeliveries()
                .collectList()
                .map(d-> APIResponse.builder()
                        .message("Deliveries retrieved successfully")
                        .status("200")
                        .data(d)
                        .build());
    }

    @DeleteMapping("/delete/{id}")
    public Mono<APIResponse> deleteDelivery(@PathVariable long id) {
        LoggingUtils.logRequest(null, null, null, DELIVERIES, "DELETE");
        return deliveryService.deleteDelivery(id)
                .map(d-> APIResponse.builder()
                        .message("Delivery deleted successfully")
                        .status("200")
                        .data(d)
                        .build());
    }

    @GetMapping("/search")
    public Mono<APIResponse> searchDeliveries(@RequestParam String trackingNo) {
        LoggingUtils.logRequest(null, null, null, DELIVERIES, "GET");
        return deliveryService.searchDeliveries(trackingNo)
                .collectList()
                .map(d-> APIResponse.builder()
                        .message("Deliveries retrieved successfully")
                        .status("200")
                        .data(d)
                        .build());
    }
}
