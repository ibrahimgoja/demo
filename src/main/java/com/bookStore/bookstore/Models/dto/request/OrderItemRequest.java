package com.bookStore.bookstore.Models.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequest(
		@NotNull(message = "orderId must not be null") UUID orderId,
		@NotNull(message = "bookId must not be null") UUID bookId,
		@NotNull @Min(value = 1, message = "quantity must be at least 1") Integer quantity,
		@NotNull @DecimalMin(value = "0.0", inclusive = false)
				@Digits(integer = 8, fraction = 2) BigDecimal unitPrice
) {
}
