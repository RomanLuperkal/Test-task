package com.warehouse.myshop.order.mapper;

import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.model.Order;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order mapToOrder(CreateOrderDto orderDto);
}
