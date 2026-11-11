package com.bookStore.bookstore.Models.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CartItemRequest(
		@NotNull(message = "userId must not be null") UUID userId,
		@NotNull(message = "bookId must not be null") UUID bookId,
		@NotNull @Min(value = 1, message = "quantity must be at least 1") Integer quantity
) {
}
