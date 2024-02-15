package com.carbon.deliverytracker.repository;

import com.carbon.deliverytracker.entity.Dispatcher;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispatcherRepository extends R2dbcRepository<Dispatcher, Long> {
}
