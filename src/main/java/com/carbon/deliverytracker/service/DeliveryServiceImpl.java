package com.carbon.deliverytracker.service;

import com.carbon.deliverytracker.dto.DeliveryDto;
import com.carbon.deliverytracker.entity.*;
import com.carbon.deliverytracker.enums.PackageStatus;
import com.carbon.deliverytracker.exception.APIException;
import com.carbon.deliverytracker.repository.*;
import com.carbon.deliverytracker.utils.logging.ErrorUtils;
import com.carbon.deliverytracker.vo.*;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCachePut;
import com.hanqunfeng.reactive.redis.cache.aop.ReactiveRedisCacheable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {
    private final ShipmentRepository shipmentRepository;
    private final AddressRepository addressRepository;
    private final DispatcherRepository dispatcherRepository;
    private final WareHouseInfoRepository wareHouseInfoRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final ActivityRepository activityRepository;

    @Override
    public Mono<DeliveryVO> createDelivery(DeliveryDto deliveryDto) {
        return shipmentRepository.findShipmentByOrderNo(deliveryDto.getOrderNo())
                .switchIfEmpty(Mono.just(
                        new Shipment()))
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/create")
                        .status("500")
                        .method("POST")
                        .error(ErrorUtils.getErrorCause(e))
                        .build())
                )
                .flatMap(shipment -> {
                    if (shipment.getId() != null) {
                        return Mono.error(APIException.builder()
                                .message("Shipment already exists")
                                .path("/create")
                                .status("409")
                                .method("POST")
                                .build());
                    }
                    return handleCreateDelivery(deliveryDto, shipment);
                });
    }

    private Mono<DeliveryVO> handleCreateDelivery(DeliveryDto deliveryDto, Shipment existingShipment) {
        return Mono.just(new Address())
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/create")
                        .status("500")
                        .method("POST")
                        .build()))
                .flatMap(address -> handleCreateDeliveryWithAddress(deliveryDto, address, existingShipment));
    }

    private Mono<DeliveryVO> handleCreateDeliveryWithAddress(DeliveryDto deliveryDto, Address existinAddress, Shipment existingShipment) {
        Address newAddress = modelMapper.map(deliveryDto.getCustomerDto().getAddressDto(), existinAddress.getClass());
        newAddress.setCreatedAt(LocalDateTime.now());
        newAddress.setAddressLine1(deliveryDto.getCustomerDto().getAddressDto().getAddressLine1());
        return addressRepository.save(newAddress)
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/create")
                        .status("500")
                        .method("POST")
                        .error(ErrorUtils.getErrorCause(e))
                        .build()))
                .flatMap(address -> customerRepository.findCustomerInfoByPhone(deliveryDto.getCustomerDto().getPhone())
                        .switchIfEmpty(Mono.just(
                                new CustomerInfo()
                        ))
                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                .message(e.getMessage())
                                .path("/create")
                                .status("500")
                                .method("POST")
                                .build()))
                        .flatMap(customerInfo -> handleCreateDeliveryWithCustomerInfo(deliveryDto, address, customerInfo, existingShipment)));
    }

    private Mono<DeliveryVO> handleCreateDeliveryWithCustomerInfo(DeliveryDto deliveryDto, Address address, CustomerInfo existingCustomer, Shipment existingShipment) {
        CustomerInfo customerInfo = modelMapper.map(deliveryDto.getCustomerDto(), existingCustomer.getClass());
        customerInfo.setAddress(address);
        customerInfo.setAddressId(address.getId());
        customerInfo.setCreatedAt(LocalDateTime.now());
        return customerRepository.save(customerInfo)
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/create")
                        .status("500")
                        .method("POST")
                        .build()))
                .flatMap(customer -> {
                    Dispatcher newDispatch = modelMapper.map(deliveryDto.getDispatcherDto(), Dispatcher.class);
                    newDispatch.setCreatedAt(LocalDateTime.now());
                    return dispatcherRepository.save(newDispatch)
                            .switchIfEmpty(Mono.just(
                                    new Dispatcher()
                            ))
                            .onErrorResume((e) -> Mono.error(APIException.builder()
                                    .message(e.getMessage())
                                    .path("/create")
                                    .status("500")
                                    .method("POST")
                                    .build()))
                            .flatMap(dispatcher -> {
                                if (dispatcher.getId() == 0) {
                                    return handleCreateDeliveryWithDispatcher(deliveryDto, customer, dispatcher, existingShipment);
                                }
                                return handleCreateDeliveryWithDispatcher(deliveryDto, customer, dispatcher, existingShipment);
                            });
                });
    }

    private Mono<DeliveryVO> handleCreateDeliveryWithDispatcher(DeliveryDto deliveryDto, CustomerInfo customer, Dispatcher dispatcher, Shipment existingShipment) {
        Dispatcher newDispatcher = modelMapper.map(deliveryDto.getDispatcherDto(), dispatcher.getClass());
        newDispatcher.setCreatedAt(LocalDateTime.now());
        return dispatcherRepository.save(newDispatcher)
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/create")
                        .status("500")
                        .method("POST")
                        .build()))
                .flatMap(dispatch -> wareHouseInfoRepository.findByName(deliveryDto.getWareHouseDto().getName())
                        .switchIfEmpty(Mono.just(
                                new WareHouseInfo()
                        ))
                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                .message(e.getMessage())
                                .path("/create")
                                .status("500")
                                .method("POST")
                                .build()))
                        .flatMap(wareHouseInfo -> handleCreateDeliveryWithWareHouseInfo(deliveryDto, customer, dispatch, wareHouseInfo, existingShipment)));
    }

    private Mono<DeliveryVO> handleCreateDeliveryWithWareHouseInfo(DeliveryDto deliveryDto, CustomerInfo customer, Dispatcher dispatch, WareHouseInfo wareHouseInfo, Shipment existingShipment) {
        Address newWareHouseAddress = modelMapper.map(deliveryDto.getWareHouseDto().getAddressDto(), Address.class);
        newWareHouseAddress.setCreatedAt(LocalDateTime.now());
        return Mono.just(new Address())
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/create")
                        .status("500")
                        .method("POST")
                        .build()))
                .flatMap(address -> {
                    Address newAddress = modelMapper.map(deliveryDto.getWareHouseDto().getAddressDto(), address.getClass());
                    newAddress.setCreatedAt(LocalDateTime.now());
                    newAddress.setAddressLine1(deliveryDto.getWareHouseDto().getAddressDto().getAddressLine1());
                    return addressRepository.save(newAddress)
                            .onErrorResume((e) -> Mono.error(APIException.builder()
                                    .message(e.getMessage())
                                    .path("/create")
                                    .status("500")
                                    .method("POST")
                                    .error(ErrorUtils.getErrorCause(e))
                                    .build()))
                            .flatMap(wareHouseAddress -> {
                                WareHouseInfo newWareHouseInfo = modelMapper.map(deliveryDto.getWareHouseDto(), wareHouseInfo.getClass());
                                newWareHouseInfo.setAddress(wareHouseAddress);
                                newWareHouseInfo.setAddressId(wareHouseAddress.getId());
                                newWareHouseInfo.setCreatedAt(LocalDateTime.now());
                                return wareHouseInfoRepository.save(newWareHouseInfo)
                                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                                .message(e.getMessage())
                                                .path("/create")
                                                .status("500")
                                                .method("POST")
                                                .error(ErrorUtils.getErrorCause(e))
                                                .build()))
                                        .flatMap(wareHouse -> {
                                            Shipment newShipment = modelMapper.map(deliveryDto, existingShipment.getClass());
                                            newShipment.setCreatedAt(LocalDateTime.now());
                                            newShipment.setCustomerId(customer.getId());
                                            newShipment.setDispatcherId(dispatch.getId());
                                            newShipment.setWarehouseId(wareHouse.getId());
                                            newShipment.setStatus(PackageStatus.WAREHOUSE);
                                            return shipmentRepository.save(newShipment)
                                                    .onErrorResume((e) -> Mono.error(APIException.builder()
                                                            .message(e.getMessage())
                                                            .path("/create")
                                                            .status("500")
                                                            .method("POST")
                                                            .error(ErrorUtils.getErrorCause(e))
                                                            .build()))
                                                    .flatMap(shipment -> {
                                                        Activity activity = new Activity();
                                                        activity.setShipmentId(shipment.getId());
                                                        activity.setAction("Delivery created");
                                                        activity.setDescription(deliveryDto.getDescription());
                                                        activity.setPackageStatus(PackageStatus.PICKED_UP);
                                                        activity.setCreatedAt(LocalDateTime.now());
                                                        return activityRepository.save(activity)
                                                                .onErrorResume((e) -> Mono.error(APIException.builder()
                                                                        .message(e.getMessage())
                                                                        .path("/create")
                                                                        .status("500")
                                                                        .method("POST")
                                                                        .error(ErrorUtils.getErrorCause(e))
                                                                        .build()))
                                                                .flatMap(activity1 -> {
                                                                    DeliveryVO deliveryVO = modelMapper.map(shipment, DeliveryVO.class);
                                                                    deliveryVO.setCustomerInfoVO(modelMapper.map(customer, CustomerInfoVO.class));
                                                                    deliveryVO.setDispatcherVO(modelMapper.map(dispatch, DispatcherVO.class));
                                                                    deliveryVO.setWareHouseInfoVO(modelMapper.map(wareHouse, WareHouseInfoVO.class));
                                                                    return Mono.just(deliveryVO);
                                                                });
                                                    });
                                        });
                            });
                });
    }

    @Override
    public Mono<DeliveryVO> updateDelivery(long id, DeliveryDto deliveryDto) {
        return shipmentRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        APIException.builder()
                                .message("Shipment does not exist")
                                .path("/update")
                                .status("404")
                                .method("PUT")
                                .build()
                ))
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/update")
                        .status("500")
                        .method("PUT")
                        .error(ErrorUtils.getErrorCause(e))
                        .build()))
                .flatMap(shipment -> handleUpdateDelivery(id, deliveryDto, shipment));
    }

    private Mono<DeliveryVO> handleUpdateDelivery(long id, DeliveryDto deliveryDto, Shipment shipment) {
        return Mono.just(new Address())
                .flatMap(address -> handleUpdateDeliveryWithAddress(id, deliveryDto, address, shipment));

    }

    private Mono<DeliveryVO> handleUpdateDeliveryWithAddress(long id, DeliveryDto deliveryDto, Address address, Shipment shipment) {
        Address newAddress = modelMapper.map(deliveryDto.getCustomerDto().getAddressDto(), address.getClass());
        newAddress.setCreatedAt(LocalDateTime.now());
        newAddress.setAddressLine1(deliveryDto.getCustomerDto().getAddressDto().getAddressLine1());
        return addressRepository.save(newAddress)
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/update")
                        .status("500")
                        .method("PUT")
                        .error(ErrorUtils.getErrorCause(e))
                        .build()))
                .flatMap(address1 -> customerRepository.findById(shipment.getCustomerId())
                        .switchIfEmpty(Mono.just(
                                new CustomerInfo()
                        ))
                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                .message(e.getMessage())
                                .path("/update")
                                .status("500")
                                .method("PUT")
                                .build()))
                        .flatMap(customerInfo -> handleUpdateDeliveryWithCustomerInfo(id, deliveryDto, address1, shipment, customerInfo)));
    }

    private Mono<DeliveryVO> handleUpdateDeliveryWithCustomerInfo(long id, DeliveryDto deliveryDto, Address address1, Shipment shipment, CustomerInfo customerInfo) {
        CustomerInfo customerInfoToBeUpdated = modelMapper.map(deliveryDto.getCustomerDto(), customerInfo.getClass());
        customerInfoToBeUpdated.setAddress(address1);
        customerInfoToBeUpdated.setAddressId(address1.getId());
        customerInfoToBeUpdated.setCreatedAt(LocalDateTime.now());
        return customerRepository.save(customerInfoToBeUpdated)
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/update")
                        .status("500")
                        .method("PUT")
                        .build()))
                .flatMap(customer -> dispatcherRepository.findById(shipment.getDispatcherId())
                        .switchIfEmpty(Mono.just(
                                new Dispatcher()
                        ))
                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                .message(e.getMessage())
                                .path("/update")
                                .status("500")
                                .method("PUT")
                                .build()))
                        .flatMap(dispatcher -> handleUpdateDeliveryWithDispatcher(id, deliveryDto, customer, dispatcher, shipment)));
    }

    private Mono<DeliveryVO> handleUpdateDeliveryWithDispatcher(long id, DeliveryDto deliveryDto, CustomerInfo customer, Dispatcher dispatcher, Shipment shipment) {
        Dispatcher dispatcherToBeUpdated = modelMapper.map(deliveryDto.getDispatcherDto(), dispatcher.getClass());
        dispatcherToBeUpdated.setCreatedAt(LocalDateTime.now());
        return dispatcherRepository.save(dispatcherToBeUpdated)
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/update")
                        .status("500")
                        .method("PUT")
                        .build()))
                .flatMap(dispatch -> wareHouseInfoRepository.findById(shipment.getWarehouseId())
                        .switchIfEmpty(Mono.just(
                                new WareHouseInfo()
                        ))
                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                .message(e.getMessage())
                                .path("/update")
                                .status("500")
                                .method("PUT")
                                .build()))
                        .flatMap(wareHouseInfo -> handleUpdateDeliveryWithWareHouseInfo(id, deliveryDto, customer, dispatch, wareHouseInfo, shipment)));
    }

    private Mono<DeliveryVO> handleUpdateDeliveryWithWareHouseInfo(long id, DeliveryDto deliveryDto, CustomerInfo customer, Dispatcher dispatch, WareHouseInfo wareHouseInfo, Shipment shipment) {
        return Mono.just(new Address())
                .flatMap(address -> {
                    Address newAddress = modelMapper.map(deliveryDto.getWareHouseDto().getAddressDto(), address.getClass());
                    newAddress.setCreatedAt(LocalDateTime.now());
                    newAddress.setAddressLine1(deliveryDto.getWareHouseDto().getAddressDto().getAddressLine1());
                    return addressRepository.save(newAddress)
                            .onErrorResume((e) -> Mono.error(APIException.builder()
                                    .message(e.getMessage())
                                    .path("/update")
                                    .status("500")
                                    .method("PUT")
                                    .error(ErrorUtils.getErrorCause(e))
                                    .build()))
                            .flatMap(wareHouseAddress -> {
                                WareHouseInfo newWareHouseInfo = modelMapper.map(deliveryDto.getWareHouseDto(), wareHouseInfo.getClass());
                                newWareHouseInfo.setAddress(wareHouseAddress);
                                newWareHouseInfo.setAddressId(wareHouseAddress.getId());
                                newWareHouseInfo.setCreatedAt(LocalDateTime.now());
                                return wareHouseInfoRepository.save(newWareHouseInfo)
                                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                                .message(e.getMessage())
                                                .path("/update")
                                                .status("500")
                                                .method("PUT")
                                                .error(ErrorUtils.getErrorCause(e))
                                                .build()))
                                        .flatMap(wareHouse -> {
                                            Shipment newShipment = modelMapper.map(deliveryDto, shipment.getClass());
                                            newShipment.setCreatedAt(LocalDateTime.now());
                                            newShipment.setCustomerId(customer.getId());
                                            newShipment.setDispatcherId(dispatch.getId());
                                            newShipment.setWarehouseId(wareHouse.getId());
                                            return shipmentRepository.save(newShipment)
                                                    .onErrorResume((e) -> Mono.error(APIException.builder()
                                                            .message(e.getMessage())
                                                            .path("/update")
                                                            .status("500")
                                                            .method("PUT")
                                                            .error(ErrorUtils.getErrorCause(e))
                                                            .build()))
                                                    .flatMap(shipment1 -> {
                                                        Activity activity = new Activity();
                                                        activity.setShipmentId(shipment1.getId());
                                                        activity.setAction("Delivery updated");
                                                        activity.setDescription(deliveryDto.getDescription());
                                                        activity.setPackageStatus(PackageStatus.PICKED_UP);
                                                        activity.setCreatedAt(LocalDateTime.now());
                                                        return activityRepository.save(activity)
                                                                .onErrorResume((e) -> Mono.error(APIException.builder()
                                                                        .message(e.getMessage())
                                                                        .path("/update")
                                                                        .status("500")
                                                                        .method("PUT")
                                                                        .error(ErrorUtils.getErrorCause(e))
                                                                        .build()))
                                                                .flatMap(activity1 -> {
                                                                    DeliveryVO deliveryVO = modelMapper.map(shipment1, DeliveryVO.class);
                                                                    deliveryVO.setCustomerInfoVO(modelMapper.map(customer, CustomerInfoVO.class));
                                                                    deliveryVO.setDispatcherVO(modelMapper.map(dispatch, DispatcherVO.class));
                                                                    deliveryVO.setWareHouseInfoVO(modelMapper.map(wareHouse, WareHouseInfoVO.class));
                                                                    return Mono.just(deliveryVO);
                                                                });
                                                    });
                                        });
                            });
                });
    }

    @Override
    @ReactiveRedisCacheable(cacheName = "getDelivery", key = "#id.toString()")
    public Mono<DeliveryVO> getDelivery(long id) {
        return shipmentRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        APIException.builder()
                                .message("Shipment does not exist")
                                .path("/get")
                                .status("404")
                                .method("GET")
                                .build()
                ))
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/get")
                        .status("500")
                        .method("GET")
                        .error(ErrorUtils.getErrorCause(e))
                        .build()))
                .flatMap(shipment -> customerRepository.findById(shipment.getCustomerId())
                        .switchIfEmpty(Mono.error(
                                APIException.builder()
                                        .message("Customer does not exist")
                                        .path("/get")
                                        .status("404")
                                        .method("GET")
                                        .build()
                        ))
                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                .message(e.getMessage())
                                .path("/get")
                                .status("500")
                                .method("GET")
                                .error(ErrorUtils.getErrorCause(e))
                                .build()))
                        .flatMap(customer -> dispatcherRepository.findById(shipment.getDispatcherId())
                                .switchIfEmpty(Mono.error(
                                        APIException.builder()
                                                .message("Dispatcher does not exist")
                                                .path("/get")
                                                .status("404")
                                                .method("GET")
                                                .build()
                                ))
                                .onErrorResume((e) -> Mono.error(APIException.builder()
                                        .message(e.getMessage())
                                        .path("/get")
                                        .status("500")
                                        .method("GET")
                                        .error(ErrorUtils.getErrorCause(e))
                                        .build()))
                                .flatMap(dispatcher -> wareHouseInfoRepository.findById(shipment.getWarehouseId())
                                        .switchIfEmpty(Mono.error(
                                                APIException.builder()
                                                        .message("WareHouse does not exist")
                                                        .path("/get")
                                                        .status("404")
                                                        .method("GET")
                                                        .build()
                                        ))
                                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                                .message(e.getMessage())
                                                .path("/get")
                                                .status("500")
                                                .method("GET")
                                                .error(ErrorUtils.getErrorCause(e))
                                                .build()))
                                        .flatMap(wareHouse -> {
                                            DeliveryVO deliveryVO = modelMapper.map(shipment, DeliveryVO.class);
                                            deliveryVO.setCustomerInfoVO(modelMapper.map(customer, CustomerInfoVO.class));
                                            deliveryVO.setDispatcherVO(modelMapper.map(dispatcher, DispatcherVO.class));
                                            deliveryVO.setWareHouseInfoVO(modelMapper.map(wareHouse, WareHouseInfoVO.class));
                                            return Mono.just(deliveryVO);
                                        }))));
    }

    @Override
    @ReactiveRedisCacheable(cacheName = "getAllDeliveries", key = "T(java.util.UUID).randomUUID().toString()")
    public Flux<DeliveryVO> getAllDeliveries() {
        return shipmentRepository.findAll()
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/get-all")
                        .status("500")
                        .method("GET")
                        .error(ErrorUtils.getErrorCause(e))
                        .build()))
                .flatMap(shipment -> customerRepository.findById(shipment.getCustomerId())
                        .switchIfEmpty(Mono.error(
                                APIException.builder()
                                        .message("Customer does not exist")
                                        .path("/get-all")
                                        .status("404")
                                        .method("GET")
                                        .build()
                        ))
                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                .message(e.getMessage())
                                .path("/get-all")
                                .status("500")
                                .method("GET")
                                .error(ErrorUtils.getErrorCause(e))
                                .build()))
                        .flatMap(customer -> dispatcherRepository.findById(shipment.getDispatcherId())
                                .switchIfEmpty(Mono.error(
                                        APIException.builder()
                                                .message("Dispatcher does not exist")
                                                .path("/get-all")
                                                .status("404")
                                                .method("GET")
                                                .build()
                                ))
                                .onErrorResume((e) -> Mono.error(APIException.builder()
                                        .message(e.getMessage())
                                        .path("/get-all")
                                        .status("500")
                                        .method("GET")
                                        .error(ErrorUtils.getErrorCause(e))
                                        .build()))
                                .flatMap(dispatcher -> wareHouseInfoRepository.findById(shipment.getWarehouseId())
                                        .switchIfEmpty(Mono.error(
                                                APIException.builder()
                                                        .message("WareHouse does not exist")
                                                        .path("/get-all")
                                                        .status("404")
                                                        .method("GET")
                                                        .build()
                                        ))
                                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                                .message(e.getMessage())
                                                .path("/get-all")
                                                .status("500")
                                                .method("GET")
                                                .error(ErrorUtils.getErrorCause(e))
                                                .build()))
                                        .flatMap(wareHouse -> {
                                            DeliveryVO deliveryVO = modelMapper.map(shipment, DeliveryVO.class);
                                            deliveryVO.setCustomerInfoVO(modelMapper.map(customer, CustomerInfoVO.class));
                                            deliveryVO.setDispatcherVO(modelMapper.map(dispatcher, DispatcherVO.class));
                                            deliveryVO.setWareHouseInfoVO(modelMapper.map(wareHouse, WareHouseInfoVO.class));
                                            return Mono.just(deliveryVO);
                                        }))));
    }

    @Override
    @ReactiveRedisCachePut(cacheName = "getDelivery", key = "#id.toString()")
    public Mono<Void> deleteDelivery(long id) {
        return shipmentRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        APIException.builder()
                                .message("Shipment does not exist")
                                .path("/delete")
                                .status("404")
                                .method("DELETE")
                                .build()
                ))
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/delete")
                        .status("500")
                        .method("DELETE")
                        .error(ErrorUtils.getErrorCause(e))
                        .build()))
                .flatMap(shipment -> shipmentRepository.delete(shipment)
                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                .message(e.getMessage())
                                .path("/delete")
                                .status("500")
                                .method("DELETE")
                                .error(ErrorUtils.getErrorCause(e))
                                .build()))
                        .then());
    }

    @Override
    public Mono<DeliveryVO> updateDeliveryStatus(long id, DeliveryDto deliveryDto) {
        return shipmentRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        APIException.builder()
                                .message("Shipment does not exist")
                                .path("/update-status")
                                .status("404")
                                .method("PUT")
                                .build()
                ))
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/update-status")
                        .status("500")
                        .method("PUT")
                        .error(ErrorUtils.getErrorCause(e))
                        .build()))
                .flatMap(shipment -> {
                    Shipment shipmentToUpdate = modelMapper.map(deliveryDto, shipment.getClass());
                    shipmentToUpdate.setUpdatedAt(LocalDateTime.now());
                    shipmentToUpdate.setStatus(deliveryDto.getStatus());
                    return shipmentRepository.save(shipmentToUpdate)
                            .onErrorResume((e) -> Mono.error(APIException.builder()
                                    .message(e.getMessage())
                                    .path("/update-status")
                                    .status("500")
                                    .method("PUT")
                                    .error(ErrorUtils.getErrorCause(e))
                                    .build()))
                            .flatMap(updatedShipment -> {
                                Activity activity = new Activity();
                                activity.setShipmentId(updatedShipment.getId());
                                activity.setAction("Delivery status updated");
                                activity.setDescription(deliveryDto.getDescription());
                                activity.setPackageStatus(deliveryDto.getStatus());
                                activity.setCreatedAt(LocalDateTime.now());
                                return activityRepository.save(activity)
                                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                                .message(e.getMessage())
                                                .path("/update-status")
                                                .status("500")
                                                .method("PUT")
                                                .error(ErrorUtils.getErrorCause(e))
                                                .build()))
                                        .flatMap(activity1 -> {
                                            DeliveryVO deliveryVO = modelMapper.map(updatedShipment, DeliveryVO.class);
                                            return Mono.just(deliveryVO);
                                        });
                            });
                });
    }

    @Override
    @ReactiveRedisCacheable(cacheName = "searchDeliveries", key = "#trackingNo")
    public Flux<DeliveryVO> searchDeliveries(String trackingNo) {
        return shipmentRepository.findShipmentByTrackingNo(trackingNo)
                .onErrorResume((e) -> Mono.error(APIException.builder()
                        .message(e.getMessage())
                        .path("/search")
                        .status("500")
                        .method("GET")
                        .error(ErrorUtils.getErrorCause(e))
                        .build()))
                .flatMap(shipment -> customerRepository.findById(shipment.getCustomerId())
                        .switchIfEmpty(Mono.error(
                                APIException.builder()
                                        .message("Customer does not exist")
                                        .path("/search")
                                        .status("404")
                                        .method("GET")
                                        .build()
                        ))
                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                .message(e.getMessage())
                                .path("/search")
                                .status("500")
                                .method("GET")
                                .error(ErrorUtils.getErrorCause(e))
                                .build()))
                        .flatMap(customer -> dispatcherRepository.findById(shipment.getDispatcherId())
                                .switchIfEmpty(Mono.error(
                                        APIException.builder()
                                                .message("Dispatcher does not exist")
                                                .path("/search")
                                                .status("404")
                                                .method("GET")
                                                .build()
                                ))
                                .onErrorResume((e) -> Mono.error(APIException.builder()
                                        .message(e.getMessage())
                                        .path("/search")
                                        .status("500")
                                        .method("GET")
                                        .error(ErrorUtils.getErrorCause(e))
                                        .build()))
                                .flatMap(dispatcher -> wareHouseInfoRepository.findById(shipment.getWarehouseId())
                                        .switchIfEmpty(Mono.error(
                                                APIException.builder()
                                                        .message("WareHouse does not exist")
                                                        .path("/search")
                                                        .status("404")
                                                        .method("GET")
                                                        .build()
                                        ))
                                        .onErrorResume((e) -> Mono.error(APIException.builder()
                                                .message(e.getMessage())
                                                .path("/search")
                                                .status("500")
                                                .method("GET")
                                                .error(ErrorUtils.getErrorCause(e))
                                                .build()))
                                        .flatMap(wareHouse -> {
                                            DeliveryVO deliveryVO = modelMapper.map(shipment, DeliveryVO.class);
                                            deliveryVO.setCustomerInfoVO(modelMapper.map(customer, CustomerInfoVO.class));
                                            deliveryVO.setDispatcherVO(modelMapper.map(dispatcher, DispatcherVO.class));
                                            deliveryVO.setWareHouseInfoVO(modelMapper.map(wareHouse, WareHouseInfoVO.class));
                                            return Mono.just(deliveryVO);
                                        }))));
    }
}
