package com.bookStore.bookstore.Models.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
		UUID id,
		UUID orderId,
		UUID bookId,
		Integer quantity,
		BigDecimal unitPrice
) {
}
