package com.carbon.deliverytracker.service;

import com.carbon.deliverytracker.dto.DeliveryDto;
import com.carbon.deliverytracker.vo.DeliveryVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DeliveryService {
    Mono<DeliveryVO> createDelivery(DeliveryDto deliveryDto);

    Mono<DeliveryVO> updateDelivery(long id, DeliveryDto deliveryDto);

    Mono<DeliveryVO> getDelivery(long id);

    Flux<DeliveryVO> getAllDeliveries();

    Mono<Void> deleteDelivery(long id);

    Mono<DeliveryVO> updateDeliveryStatus(long id, DeliveryDto deliveryDto);

    Flux<DeliveryVO> searchDeliveries(String trackingNo);
}
