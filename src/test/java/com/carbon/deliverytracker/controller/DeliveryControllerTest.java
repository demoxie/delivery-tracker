package com.carbon.deliverytracker.controller;

import com.carbon.deliverytracker.dto.DeliveryDto;
import com.carbon.deliverytracker.entity.Shipment;
import com.carbon.deliverytracker.enums.PackageStatus;
import com.carbon.deliverytracker.repository.*;
import com.carbon.deliverytracker.service.DeliveryService;
import com.carbon.deliverytracker.service.DeliveryServiceImpl;
import com.carbon.deliverytracker.vo.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.reactivestreams.Publisher;
import org.springframework.http.server.reactive.ChannelSendOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.function.Function;

import static org.mockito.Mockito.*;

class DeliveryControllerTest {
    /**
     * Method under test: {@link DeliveryController#createDelivery(DeliveryDto)}
     */
    @Test
    void testCreateDelivery() {
        ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(shipmentRepository.findShipmentByOrderNo(Mockito.<String>any())).thenReturn(justResult);
        AddressRepository addressRepository = mock(AddressRepository.class);
        DispatcherRepository dispatcherRepository = mock(DispatcherRepository.class);
        WareHouseInfoRepository wareHouseInfoRepository = mock(WareHouseInfoRepository.class);
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        DeliveryController deliveryController = new DeliveryController(
                new DeliveryServiceImpl(shipmentRepository, addressRepository, dispatcherRepository, wareHouseInfoRepository,
                        customerRepository, new ModelMapper(), mock(ActivityRepository.class)));

        deliveryController.createDelivery(new DeliveryDto());

        verify(shipmentRepository).findShipmentByOrderNo(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DeliveryController#createDelivery(DeliveryDto)}
     */
    @Test
    void testCreateDelivery2() {
       
        DeliveryService deliveryService = mock(DeliveryService.class);
        DeliveryVO.DeliveryVOBuilder builderResult = DeliveryVO.builder();
        ActivityVO activityVO = ActivityVO.builder()
                .action("Action")
                .actionBy("Action By")
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .description("The characteristics of someone or something")
                .id(1L)
                .packageStatus(PackageStatus.PICKED_UP)
                .shipmentId(1L)
                .updatedAt("2020-03-01")
                .build();
        DeliveryVO.DeliveryVOBuilder createdAtResult = builderResult.activityVO(activityVO)
                .createdAt("Jan 1, 2020 8:00am GMT+0100");
        CustomerInfoVO customerInfoVO = CustomerInfoVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .email("jane.doe@example.org")
                .firstName("Jane")
                .phone("6625550144")
                .updatedAt("2020-03-01")
                .username("janedoe")
                .build();
        DeliveryVO.DeliveryVOBuilder descriptionResult = createdAtResult.customerInfoVO(customerInfoVO)
                .deliveryDateRange("2020-03-01")
                .deliveryStatus("Delivery Status")
                .description("The characteristics of someone or something");
        DispatcherVO dispatcherVO = DispatcherVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .email("jane.doe@example.org")
                .firstName("Jane")
                .password("iloveyou")
                .phone("6625550144")
                .updatedAt("2020-03-01")
                .username("janedoe")
                .build();
        DeliveryVO.DeliveryVOBuilder updatedAtResult = descriptionResult.dispatcherVO(dispatcherVO)
                .id(1L)
                .name("Name")
                .orderNo("Order No")
                .priority("Priority")
                .shipmentType("Shipment Type")
                .status("Status")
                .trackingNo("Tracking No")
                .updatedAt("2020-03-01");
        WareHouseInfoVO wareHouseInfoVO = WareHouseInfoVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .name("Name")
                .updatedAt("2020-03-01")
                .build();
        DeliveryVO buildResult = updatedAtResult.wareHouseInfoVO(wareHouseInfoVO).build();
        Mono<DeliveryVO> justResult = Mono.just(buildResult);
        when(deliveryService.createDelivery(Mockito.<DeliveryDto>any())).thenReturn(justResult);
        DeliveryController deliveryController = new DeliveryController(deliveryService);

        deliveryController.createDelivery(new DeliveryDto());

        verify(deliveryService).createDelivery(Mockito.<DeliveryDto>any());
    }

    /**
     * Method under test:
     * {@link DeliveryController#updateDelivery(long, DeliveryDto)}
     */
    @Test
    void testUpdateDelivery() {

        ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(justResult);
        AddressRepository addressRepository = mock(AddressRepository.class);
        DispatcherRepository dispatcherRepository = mock(DispatcherRepository.class);
        WareHouseInfoRepository wareHouseInfoRepository = mock(WareHouseInfoRepository.class);
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        DeliveryController deliveryController = new DeliveryController(
                new DeliveryServiceImpl(shipmentRepository, addressRepository, dispatcherRepository, wareHouseInfoRepository,
                        customerRepository, new ModelMapper(), mock(ActivityRepository.class)));

        deliveryController.updateDelivery(1L, new DeliveryDto());

        verify(shipmentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link DeliveryController#updateDelivery(long, DeliveryDto)}
     */
    @Test
    void testUpdateDelivery2() {

        DeliveryService deliveryService = mock(DeliveryService.class);
        DeliveryVO.DeliveryVOBuilder builderResult = DeliveryVO.builder();
        ActivityVO activityVO = ActivityVO.builder()
                .action("Action")
                .actionBy("Action By")
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .description("The characteristics of someone or something")
                .id(1L)
                .packageStatus(PackageStatus.PICKED_UP)
                .shipmentId(1L)
                .updatedAt("2020-03-01")
                .build();
        DeliveryVO.DeliveryVOBuilder createdAtResult = builderResult.activityVO(activityVO)
                .createdAt("Jan 1, 2020 8:00am GMT+0100");
        CustomerInfoVO customerInfoVO = CustomerInfoVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .email("jane.doe@example.org")
                .firstName("Jane")
                .phone("6625550144")
                .updatedAt("2020-03-01")
                .username("janedoe")
                .build();
        DeliveryVO.DeliveryVOBuilder descriptionResult = createdAtResult.customerInfoVO(customerInfoVO)
                .deliveryDateRange("2020-03-01")
                .deliveryStatus("Delivery Status")
                .description("The characteristics of someone or something");
        DispatcherVO dispatcherVO = DispatcherVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .email("jane.doe@example.org")
                .firstName("Jane")
                .password("iloveyou")
                .phone("6625550144")
                .updatedAt("2020-03-01")
                .username("janedoe")
                .build();
        DeliveryVO.DeliveryVOBuilder updatedAtResult = descriptionResult.dispatcherVO(dispatcherVO)
                .id(1L)
                .name("Name")
                .orderNo("Order No")
                .priority("Priority")
                .shipmentType("Shipment Type")
                .status("Status")
                .trackingNo("Tracking No")
                .updatedAt("2020-03-01");
        WareHouseInfoVO wareHouseInfoVO = WareHouseInfoVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .name("Name")
                .updatedAt("2020-03-01")
                .build();
        DeliveryVO buildResult = updatedAtResult.wareHouseInfoVO(wareHouseInfoVO).build();
        Mono<DeliveryVO> justResult = Mono.just(buildResult);
        when(deliveryService.updateDelivery(anyLong(), Mockito.<DeliveryDto>any())).thenReturn(justResult);
        DeliveryController deliveryController = new DeliveryController(deliveryService);

        deliveryController.updateDelivery(1L, new DeliveryDto());

        verify(deliveryService).updateDelivery(anyLong(), Mockito.<DeliveryDto>any());
    }

    /**
     * Method under test:
     * {@link DeliveryController#updateDeliveryStatus(long, DeliveryDto)}
     */
    @Test
    void testUpdateDeliveryStatus() {

        ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(justResult);
        AddressRepository addressRepository = mock(AddressRepository.class);
        DispatcherRepository dispatcherRepository = mock(DispatcherRepository.class);
        WareHouseInfoRepository wareHouseInfoRepository = mock(WareHouseInfoRepository.class);
        CustomerRepository customerRepository = mock(CustomerRepository.class);
        DeliveryController deliveryController = new DeliveryController(
                new DeliveryServiceImpl(shipmentRepository, addressRepository, dispatcherRepository, wareHouseInfoRepository,
                        customerRepository, new ModelMapper(), mock(ActivityRepository.class)));

        deliveryController.updateDeliveryStatus(1L, new DeliveryDto());

        verify(shipmentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link DeliveryController#updateDeliveryStatus(long, DeliveryDto)}
     */
    @Test
    void testUpdateDeliveryStatus2() {

        DeliveryService deliveryService = mock(DeliveryService.class);
        DeliveryVO.DeliveryVOBuilder builderResult = DeliveryVO.builder();
        ActivityVO activityVO = ActivityVO.builder()
                .action("Action")
                .actionBy("Action By")
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .description("The characteristics of someone or something")
                .id(1L)
                .packageStatus(PackageStatus.PICKED_UP)
                .shipmentId(1L)
                .updatedAt("2020-03-01")
                .build();
        DeliveryVO.DeliveryVOBuilder createdAtResult = builderResult.activityVO(activityVO)
                .createdAt("Jan 1, 2020 8:00am GMT+0100");
        CustomerInfoVO customerInfoVO = CustomerInfoVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .email("jane.doe@example.org")
                .firstName("Jane")
                .phone("6625550144")
                .updatedAt("2020-03-01")
                .username("janedoe")
                .build();
        DeliveryVO.DeliveryVOBuilder descriptionResult = createdAtResult.customerInfoVO(customerInfoVO)
                .deliveryDateRange("2020-03-01")
                .deliveryStatus("Delivery Status")
                .description("The characteristics of someone or something");
        DispatcherVO dispatcherVO = DispatcherVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .email("jane.doe@example.org")
                .firstName("Jane")
                .password("iloveyou")
                .phone("6625550144")
                .updatedAt("2020-03-01")
                .username("janedoe")
                .build();
        DeliveryVO.DeliveryVOBuilder updatedAtResult = descriptionResult.dispatcherVO(dispatcherVO)
                .id(1L)
                .name("Name")
                .orderNo("Order No")
                .priority("Priority")
                .shipmentType("Shipment Type")
                .status("Status")
                .trackingNo("Tracking No")
                .updatedAt("2020-03-01");
        WareHouseInfoVO wareHouseInfoVO = WareHouseInfoVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .name("Name")
                .updatedAt("2020-03-01")
                .build();
        DeliveryVO buildResult = updatedAtResult.wareHouseInfoVO(wareHouseInfoVO).build();
        Mono<DeliveryVO> justResult = Mono.just(buildResult);
        when(deliveryService.updateDeliveryStatus(anyLong(), Mockito.<DeliveryDto>any())).thenReturn(justResult);
        DeliveryController deliveryController = new DeliveryController(deliveryService);

        deliveryController.updateDeliveryStatus(1L, new DeliveryDto());

        verify(deliveryService).updateDeliveryStatus(anyLong(), Mockito.<DeliveryDto>any());
    }

    /**
     * Method under test: {@link DeliveryController#getDelivery(long)}
     */
    @Test
    void testGetDelivery() {

        ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(justResult);
        AddressRepository addressRepository = mock(AddressRepository.class);
        DispatcherRepository dispatcherRepository = mock(DispatcherRepository.class);
        WareHouseInfoRepository wareHouseInfoRepository = mock(WareHouseInfoRepository.class);
        CustomerRepository customerRepository = mock(CustomerRepository.class);

        (new DeliveryController(new DeliveryServiceImpl(shipmentRepository, addressRepository, dispatcherRepository,
                wareHouseInfoRepository, customerRepository, new ModelMapper(), mock(ActivityRepository.class))))
                .getDelivery(1L);

        verify(shipmentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DeliveryController#getDelivery(long)}
     */
    @Test
    void testGetDelivery2() {

        DeliveryService deliveryService = mock(DeliveryService.class);
        DeliveryVO.DeliveryVOBuilder builderResult = DeliveryVO.builder();
        ActivityVO activityVO = ActivityVO.builder()
                .action("Action")
                .actionBy("Action By")
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .description("The characteristics of someone or something")
                .id(1L)
                .packageStatus(PackageStatus.PICKED_UP)
                .shipmentId(1L)
                .updatedAt("2020-03-01")
                .build();
        DeliveryVO.DeliveryVOBuilder createdAtResult = builderResult.activityVO(activityVO)
                .createdAt("Jan 1, 2020 8:00am GMT+0100");
        CustomerInfoVO customerInfoVO = CustomerInfoVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .email("jane.doe@example.org")
                .firstName("Jane")
                .phone("6625550144")
                .updatedAt("2020-03-01")
                .username("janedoe")
                .build();
        DeliveryVO.DeliveryVOBuilder descriptionResult = createdAtResult.customerInfoVO(customerInfoVO)
                .deliveryDateRange("2020-03-01")
                .deliveryStatus("Delivery Status")
                .description("The characteristics of someone or something");
        DispatcherVO dispatcherVO = DispatcherVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .email("jane.doe@example.org")
                .firstName("Jane")
                .password("iloveyou")
                .phone("6625550144")
                .updatedAt("2020-03-01")
                .username("janedoe")
                .build();
        DeliveryVO.DeliveryVOBuilder updatedAtResult = descriptionResult.dispatcherVO(dispatcherVO)
                .id(1L)
                .name("Name")
                .orderNo("Order No")
                .priority("Priority")
                .shipmentType("Shipment Type")
                .status("Status")
                .trackingNo("Tracking No")
                .updatedAt("2020-03-01");
        WareHouseInfoVO wareHouseInfoVO = WareHouseInfoVO.builder()
                .createdAt("Jan 1, 2020 8:00am GMT+0100")
                .name("Name")
                .updatedAt("2020-03-01")
                .build();
        DeliveryVO buildResult = updatedAtResult.wareHouseInfoVO(wareHouseInfoVO).build();
        Mono<DeliveryVO> justResult = Mono.just(buildResult);
        when(deliveryService.getDelivery(anyLong())).thenReturn(justResult);

        (new DeliveryController(deliveryService)).getDelivery(1L);

        verify(deliveryService).getDelivery(anyLong());
    }

    /**
     * Method under test: {@link DeliveryController#getAllDeliveries()}
     */
    @Test
    void testGetAllDeliveries() {

        ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
        Flux<Shipment> fromIterableResult = Flux.fromIterable(new ArrayList<>());
        when(shipmentRepository.findAll()).thenReturn(fromIterableResult);
        AddressRepository addressRepository = mock(AddressRepository.class);
        DispatcherRepository dispatcherRepository = mock(DispatcherRepository.class);
        WareHouseInfoRepository wareHouseInfoRepository = mock(WareHouseInfoRepository.class);
        CustomerRepository customerRepository = mock(CustomerRepository.class);

        (new DeliveryController(new DeliveryServiceImpl(shipmentRepository, addressRepository, dispatcherRepository,
                wareHouseInfoRepository, customerRepository, new ModelMapper(), mock(ActivityRepository.class))))
                .getAllDeliveries();

        verify(shipmentRepository).findAll();
    }

    /**
     * Method under test: {@link DeliveryController#getAllDeliveries()}
     */
    @Test
    void testGetAllDeliveries2() {

        Flux<Shipment> flux = mock(Flux.class);
        Flux<Shipment> fromIterableResult = Flux.fromIterable(new ArrayList<>());
        when(flux.onErrorResume(Mockito.<Function<Throwable, Publisher<Shipment>>>any())).thenReturn(fromIterableResult);
        ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
        when(shipmentRepository.findAll()).thenReturn(flux);
        AddressRepository addressRepository = mock(AddressRepository.class);
        DispatcherRepository dispatcherRepository = mock(DispatcherRepository.class);
        WareHouseInfoRepository wareHouseInfoRepository = mock(WareHouseInfoRepository.class);
        CustomerRepository customerRepository = mock(CustomerRepository.class);

        (new DeliveryController(new DeliveryServiceImpl(shipmentRepository, addressRepository, dispatcherRepository,
                wareHouseInfoRepository, customerRepository, new ModelMapper(), mock(ActivityRepository.class))))
                .getAllDeliveries();

        verify(shipmentRepository).findAll();
        verify(flux).onErrorResume(Mockito.<Function<Throwable, Publisher<Shipment>>>any());
    }

    /**
     * Method under test: {@link DeliveryController#deleteDelivery(long)}
     */
    @Test
    void testDeleteDelivery() {

        ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
        Mono<Shipment> justResult = Mono.just(new Shipment());
        when(shipmentRepository.findById(Mockito.<Long>any())).thenReturn(justResult);
        AddressRepository addressRepository = mock(AddressRepository.class);
        DispatcherRepository dispatcherRepository = mock(DispatcherRepository.class);
        WareHouseInfoRepository wareHouseInfoRepository = mock(WareHouseInfoRepository.class);
        CustomerRepository customerRepository = mock(CustomerRepository.class);

        (new DeliveryController(new DeliveryServiceImpl(shipmentRepository, addressRepository, dispatcherRepository,
                wareHouseInfoRepository, customerRepository, new ModelMapper(), mock(ActivityRepository.class))))
                .deleteDelivery(1L);

        verify(shipmentRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link DeliveryController#deleteDelivery(long)}
     */
    @Test
    void testDeleteDelivery2() {

        DeliveryService deliveryService = mock(DeliveryService.class);
        Flux<?> source = Flux.fromIterable(new ArrayList<>());
        when(deliveryService.deleteDelivery(anyLong())).thenReturn(new ChannelSendOperator<>(source, mock(Function.class)));

        (new DeliveryController(deliveryService)).deleteDelivery(1L);

        verify(deliveryService).deleteDelivery(anyLong());
    }


    /**
     * Method under test: {@link DeliveryController#searchDeliveries(String)}
     */
    @Test
    void testSearchDeliveries() {
        ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
        Flux<Shipment> fromIterableResult = Flux.fromIterable(new ArrayList<>());
        when(shipmentRepository.findShipmentByTrackingNo(Mockito.<String>any())).thenReturn(fromIterableResult);
        AddressRepository addressRepository = mock(AddressRepository.class);
        DispatcherRepository dispatcherRepository = mock(DispatcherRepository.class);
        WareHouseInfoRepository wareHouseInfoRepository = mock(WareHouseInfoRepository.class);
        CustomerRepository customerRepository = mock(CustomerRepository.class);

        (new DeliveryController(new DeliveryServiceImpl(shipmentRepository, addressRepository, dispatcherRepository,
                wareHouseInfoRepository, customerRepository, new ModelMapper(), mock(ActivityRepository.class))))
                .searchDeliveries("Tracking No");

        verify(shipmentRepository).findShipmentByTrackingNo(Mockito.<String>any());
    }

    /**
     * Method under test: {@link DeliveryController#searchDeliveries(String)}
     */
    @Test
    void testSearchDeliveries2() {

        Flux<Shipment> flux = mock(Flux.class);
        Flux<Shipment> fromIterableResult = Flux.fromIterable(new ArrayList<>());
        when(flux.onErrorResume(Mockito.<Function<Throwable, Publisher<Shipment>>>any())).thenReturn(fromIterableResult);
        ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
        when(shipmentRepository.findShipmentByTrackingNo(Mockito.<String>any())).thenReturn(flux);
        AddressRepository addressRepository = mock(AddressRepository.class);
        DispatcherRepository dispatcherRepository = mock(DispatcherRepository.class);
        WareHouseInfoRepository wareHouseInfoRepository = mock(WareHouseInfoRepository.class);
        CustomerRepository customerRepository = mock(CustomerRepository.class);

        (new DeliveryController(new DeliveryServiceImpl(shipmentRepository, addressRepository, dispatcherRepository,
                wareHouseInfoRepository, customerRepository, new ModelMapper(), mock(ActivityRepository.class))))
                .searchDeliveries("Tracking No");

        verify(shipmentRepository).findShipmentByTrackingNo(Mockito.<String>any());
        verify(flux).onErrorResume(Mockito.<Function<Throwable, Publisher<Shipment>>>any());
    }
}
