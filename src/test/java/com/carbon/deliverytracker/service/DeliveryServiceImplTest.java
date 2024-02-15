package com.carbon.deliverytracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.carbon.deliverytracker.dto.DeliveryDto;
import com.carbon.deliverytracker.entity.Shipment;
import com.carbon.deliverytracker.enums.PackageStatus;
import com.carbon.deliverytracker.enums.ShipmentPriority;
import com.carbon.deliverytracker.repository.ActivityRepository;
import com.carbon.deliverytracker.repository.AddressRepository;
import com.carbon.deliverytracker.repository.CustomerRepository;
import com.carbon.deliverytracker.repository.DispatcherRepository;
import com.carbon.deliverytracker.repository.ShipmentRepository;
import com.carbon.deliverytracker.repository.WareHouseInfoRepository;
import com.carbon.deliverytracker.vo.DeliveryVO;

import java.util.ArrayList;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ContextConfiguration(classes = {DeliveryServiceImpl.class})
@ExtendWith(SpringExtension.class)
class DeliveryServiceImplTest {
    @MockBean
    private ActivityRepository activityRepository;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private DeliveryServiceImpl deliveryServiceImpl;

    @MockBean
    private DispatcherRepository dispatcherRepository;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private ShipmentRepository shipmentRepository;

    @MockBean
    private WareHouseInfoRepository wareHouseInfoRepository;

    /**
     * Method under test: {@link DeliveryServiceImpl#createDelivery(DeliveryDto)}
     */
    @Test
    void testCreateDelivery() {
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(shipmentRepository.findShipmentByOrderNo(Mockito.<String>any())).thenReturn(justResult);
        
        deliveryServiceImpl.createDelivery(new DeliveryDto());

        
        verify(shipmentRepository).findShipmentByOrderNo(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DeliveryServiceImpl#createDelivery(DeliveryDto)}
     */
    @Test
    void testCreateDelivery2() {
        
        Mono<Shipment> justResult = Mono
                .just(new Shipment("Name", "The characteristics of someone or something", PackageStatus.PICKED_UP,
                        "Shipment Type", ShipmentPriority.HIGH, "2020-03-01", 1L, "Order No", "Tracking No", 1L, 1L));
        when(shipmentRepository.findShipmentByOrderNo(Mockito.<String>any())).thenReturn(justResult);

        
        deliveryServiceImpl.createDelivery(new DeliveryDto());

        
        verify(shipmentRepository).findShipmentByOrderNo(Mockito.<String>any());
    }


    /**
     * Method under test:
     * {@link DeliveryServiceImpl#updateDelivery(long, DeliveryDto)}
     */
    @Test
    void testUpdateDelivery() {
        
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(justResult);

        
        deliveryServiceImpl.updateDelivery(1L, new DeliveryDto());

        
        verify(shipmentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link DeliveryServiceImpl#updateDelivery(long, DeliveryDto)}
     */
    @Test
    void testUpdateDelivery2() {
        
        Mono<Shipment> mono = mock(Mono.class);
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(mono.switchIfEmpty(Mockito.<Mono<Shipment>>any())).thenReturn(justResult);
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(mono);

        
        deliveryServiceImpl.updateDelivery(1L, new DeliveryDto());

        
        verify(shipmentRepository).findById(Mockito.<Long>any());
        verify(mono).switchIfEmpty(Mockito.<Mono<Shipment>>any());
    }

    /**
     * Method under test: {@link DeliveryServiceImpl#getDelivery(long)}
     */
    @Test
    void testGetDelivery() {
        
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(justResult);

        
        deliveryServiceImpl.getDelivery(1L);

        
        verify(shipmentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DeliveryServiceImpl#getDelivery(long)}
     */
    @Test
    void testGetDelivery2() {
        
        Mono<Shipment> mono = mock(Mono.class);
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(mono.switchIfEmpty(Mockito.<Mono<Shipment>>any())).thenReturn(justResult);
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(mono);

        
        deliveryServiceImpl.getDelivery(1L);

        
        verify(shipmentRepository).findById(Mockito.<Long>any());
        verify(mono).switchIfEmpty(Mockito.<Mono<Shipment>>any());
    }

    /**
     * Method under test: {@link DeliveryServiceImpl#getAllDeliveries()}
     */
    @Test
    void testGetAllDeliveries() {
        
        Flux<Shipment> fromIterableResult = Flux.fromIterable(new ArrayList<>());
        when(shipmentRepository.findAll()).thenReturn(fromIterableResult);

        
        Flux<DeliveryVO> actualAllDeliveries = deliveryServiceImpl.getAllDeliveries();

        
        verify(shipmentRepository).findAll();
        assertEquals(Integer.SIZE, actualAllDeliveries.getPrefetch());
    }

    /**
     * Method under test: {@link DeliveryServiceImpl#getAllDeliveries()}
     */
    @Test
    void testGetAllDeliveries2() {
        
        Flux<Shipment> flux = mock(Flux.class);
        Flux<Shipment> fromIterableResult = Flux.fromIterable(new ArrayList<>());
        when(flux.onErrorResume(Mockito.<Function<Throwable, Publisher<Shipment>>>any())).thenReturn(fromIterableResult);
        when(shipmentRepository.findAll()).thenReturn(flux);

        
        Flux<DeliveryVO> actualAllDeliveries = deliveryServiceImpl.getAllDeliveries();

        
        verify(shipmentRepository).findAll();
        verify(flux).onErrorResume(Mockito.<Function<Throwable, Publisher<Shipment>>>any());
        assertEquals(Integer.SIZE, actualAllDeliveries.getPrefetch());
    }

    /**
     * Method under test: {@link DeliveryServiceImpl#deleteDelivery(long)}
     */
    @Test
    void testDeleteDelivery() {
        
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(justResult);

        
        deliveryServiceImpl.deleteDelivery(1L);

        
        verify(shipmentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DeliveryServiceImpl#deleteDelivery(long)}
     */
    @Test
    void testDeleteDelivery2() {
        
        Mono<Shipment> mono = mock(Mono.class);
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(mono.switchIfEmpty(Mockito.<Mono<Shipment>>any())).thenReturn(justResult);
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(mono);

        
        deliveryServiceImpl.deleteDelivery(1L);

        
        verify(shipmentRepository).findById(Mockito.<Long>any());
        verify(mono).switchIfEmpty(Mockito.<Mono<Shipment>>any());
    }

    /**
     * Method under test:
     * {@link DeliveryServiceImpl#updateDeliveryStatus(long, DeliveryDto)}
     */
    @Test
    void testUpdateDeliveryStatus() {
        
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(justResult);

        
        deliveryServiceImpl.updateDeliveryStatus(1L, new DeliveryDto());

        
        verify(shipmentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link DeliveryServiceImpl#updateDeliveryStatus(long, DeliveryDto)}
     */
    @Test
    void testUpdateDeliveryStatus2() {
        
        Mono<Shipment> mono = mock(Mono.class);
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(mono.switchIfEmpty(Mockito.<Mono<Shipment>>any())).thenReturn(justResult);
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(mono);

        
        deliveryServiceImpl.updateDeliveryStatus(1L, new DeliveryDto());

        
        verify(shipmentRepository).findById(Mockito.<Long>any());
        verify(mono).switchIfEmpty(Mockito.<Mono<Shipment>>any());
    }

    /**
     * Method under test: {@link DeliveryServiceImpl#searchDeliveries(String)}
     */
    @Test
    void testSearchDeliveries() {
        
        Flux<Shipment> fromIterableResult = Flux.fromIterable(new ArrayList<>());
        when(shipmentRepository.findShipmentByTrackingNo(Mockito.<String>any())).thenReturn(fromIterableResult);

        
        Flux<DeliveryVO> actualSearchDeliveriesResult = deliveryServiceImpl.searchDeliveries("Tracking No");

        
        verify(shipmentRepository).findShipmentByTrackingNo(Mockito.<String>any());
        assertEquals(Integer.SIZE, actualSearchDeliveriesResult.getPrefetch());
    }

    /**
     * Method under test: {@link DeliveryServiceImpl#searchDeliveries(String)}
     */
    @Test
    void testSearchDeliveries2() {
        
        Flux<Shipment> flux = mock(Flux.class);
        Flux<Shipment> fromIterableResult = Flux.fromIterable(new ArrayList<>());
        when(flux.onErrorResume(Mockito.<Function<Throwable, Publisher<Shipment>>>any())).thenReturn(fromIterableResult);
        when(shipmentRepository.findShipmentByTrackingNo(Mockito.<String>any())).thenReturn(flux);

        
        Flux<DeliveryVO> actualSearchDeliveriesResult = deliveryServiceImpl.searchDeliveries("Tracking No");

        
        verify(shipmentRepository).findShipmentByTrackingNo(Mockito.<String>any());
        verify(flux).onErrorResume(Mockito.<Function<Throwable, Publisher<Shipment>>>any());
        assertEquals(Integer.SIZE, actualSearchDeliveriesResult.getPrefetch());
    }

}
