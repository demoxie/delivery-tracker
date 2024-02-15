package com.carbon.deliverytracker.repository;

import com.carbon.deliverytracker.entity.WareHouseInfo;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WareHouseInfoRepository extends R2dbcRepository<WareHouseInfo, Long> {
    Mono<WareHouseInfo> findByName(String name);
}
