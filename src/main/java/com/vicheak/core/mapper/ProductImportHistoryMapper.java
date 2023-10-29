package com.vicheak.core.mapper;

import com.vicheak.core.dto.ProductDto;
import com.vicheak.core.dto.ProductImportDto;
import com.vicheak.core.entity.Product;
import com.vicheak.core.entity.ProductImportHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImportHistoryMapper {

    @Mapping(target = "product.id", source = "productId")
    ProductImportHistory toProductImportHistory(ProductImportDto productImportDto);

    @Mapping(target = "productId", source = "product.id")
    ProductImportDto toProductImportDto(ProductImportHistory productImportHistory);

    List<ProductImportDto> toProductImportDto(List<ProductImportHistory> productImportHistories);

    @Mapping(target = "color", source = "color.name")
    @Mapping(target = "productImportDtoList", source = "productImportHistories")
    ProductDto toProductDto(Product product);

}
