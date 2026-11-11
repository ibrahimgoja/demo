package com.bookStore.bookstore.Models.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record BookResponse(
		UUID id,
		String title,
		String author,
		String isbn,
		BigDecimal price,
		Integer stock,
		String description
) {
}
