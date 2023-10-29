package com.vicheak.core.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SalePriceDto(@NotNull @DecimalMin(value = "0.000001") BigDecimal salePrice) {
}
