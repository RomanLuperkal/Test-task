package com.warehouse.myshop.product.mapper;

import com.warehouse.myshop.category.mapper.CategoryMapper;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.model.Product;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    Product mapToProduct(NewProductDto productDto);

    ResponseProductDto mapToResponseProductDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    Product mapToProduct(@MappingTarget Product product, UpdateProductDto updateProduct);

    List<ResponseProductDto> mapToListResponseProductDto(Page<Product> page);
}
