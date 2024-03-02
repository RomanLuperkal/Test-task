package com.warehouse.myshop.product.mapper;

import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product mapToProduct(NewProductDto productDto);

    ResponseProductDto mapToResponseProductDto(Product product);
}
