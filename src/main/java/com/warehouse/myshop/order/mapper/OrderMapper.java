package com.warehouse.myshop.order.mapper;

import com.warehouse.myshop.order.dto.CreateOrderDto;
import com.warehouse.myshop.order.model.Ordering;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OrderMapper {
    Ordering mapToOrder(CreateOrderDto orderDto);
}
