package com.vicheak.core.mapper;

import com.vicheak.core.dto.ProductDto;
import com.vicheak.core.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ProductImportHistoryMapper.class})
public interface ProductMapper {

    List<ProductDto> toProductDto(List<Product> products);

}
