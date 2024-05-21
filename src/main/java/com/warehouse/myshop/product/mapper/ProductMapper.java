package com.warehouse.myshop.product.mapper;

import com.warehouse.myshop.category.mapper.CategoryMapper;
import com.warehouse.myshop.product.dto.condition.FilterConditionDto;
import com.warehouse.myshop.product.dto.NewProductDto;
import com.warehouse.myshop.product.dto.ResponseProductDto;
import com.warehouse.myshop.product.dto.UpdateProductDto;
import com.warehouse.myshop.product.model.Product;
import org.mapstruct.*;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {
    @Mapping(target = "category", ignore = true)
    Product mapToProduct(NewProductDto productDto);

    ResponseProductDto mapToResponseProductDto(Product product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "category", ignore = true)
    Product mapToProduct(@MappingTarget Product product, UpdateProductDto updateProduct);

    List<ResponseProductDto> mapToListResponseProductDto(Page<Product> page);

    default List<Specification<Product>> mapToListSpecification(List<FilterConditionDto<?>> listConditions) {
        return listConditions.stream().map(FilterConditionDto::getSpecification).collect(Collectors.toList());
    }
}
