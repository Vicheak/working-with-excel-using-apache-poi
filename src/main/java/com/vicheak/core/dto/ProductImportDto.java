package com.vicheak.core.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductImportDto(@NotNull @Positive
                               Long productId,
                               @NotNull
                               LocalDateTime importDate,
                               @NotNull @Positive @Min(value = 1)
                               Integer importUnit,
                               @NotNull @Positive @DecimalMin(value = "0.000001")
                               BigDecimal importPrice) {
}
