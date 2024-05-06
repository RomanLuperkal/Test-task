package com.warehouse.myshop.order.mapper;

import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.dto.ResponseOrderDto;
import com.warehouse.myshop.order.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order mapToOrder(CreateOrderDto orderDto);
    @Mapping(source = "customer.id", target = "customerId")
    ResponseOrderDto mapToResponseOrderDto(Order order);
}
