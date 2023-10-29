package com.vicheak.core.base;

import lombok.Builder;

@Builder
public record FieldError(String field,
                         String message) {
}
