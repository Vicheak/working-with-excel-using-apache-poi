package com.vicheak.core.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductDto(String name,
                         Integer availableStock,
                         BigDecimal salePrice,
                         String color,
                         List<ProductImportDto> productImportDtoList) {
}
