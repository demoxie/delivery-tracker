package com.carbon.deliverytracker.repository;

import com.carbon.deliverytracker.entity.CustomerInfo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends R2dbcRepository<CustomerInfo, Long> {
    @Query("SELECT * FROM customer_info WHERE phone = :phone")
    Mono<CustomerInfo> findCustomerInfoByPhone(String phone);
}
