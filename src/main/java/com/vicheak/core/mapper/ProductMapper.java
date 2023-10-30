package com.vicheak.core.mapper;

import com.vicheak.core.dto.ProductDto;
import com.vicheak.core.dto.UpdateProductDto;
import com.vicheak.core.entity.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductImportHistoryMapper.class})
public interface ProductMapper {

    List<ProductDto> toProductDto(List<Product> products);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateProductDtoToProduct(@MappingTarget Product product, UpdateProductDto updateProductDto);

}
