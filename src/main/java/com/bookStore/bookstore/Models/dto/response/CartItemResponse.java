package com.bookStore.bookstore.Models.dto.response;

import java.util.UUID;

public record CartItemResponse(
		UUID id,
		UUID userId,
		UUID bookId,
		Integer quantity
) {
}
