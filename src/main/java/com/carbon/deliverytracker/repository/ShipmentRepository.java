package com.carbon.deliverytracker.repository;

import com.carbon.deliverytracker.entity.Shipment;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ShipmentRepository extends R2dbcRepository<Shipment, Long> {
    @Query("SELECT * FROM shipment WHERE order_no = :s")
    Mono<Shipment> findShipmentByOrderNo(String s);

    Flux<Shipment> findShipmentByTrackingNo(String trackingNo);
}
