package com.bookStore.bookstore.Models.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
		UUID id,
		UUID userId,
		LocalDateTime orderedAt,
		BigDecimal totalPrice
) {
}
