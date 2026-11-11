package com.bookStore.bookstore.Models.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record BookRequest(
		@NotBlank @Size(max = 255) String title,
		@NotBlank @Size(max = 255) String author,
		@NotBlank @Size(max = 20) String isbn,
		@NotNull @DecimalMin(value = "0.0", inclusive = true)
				@Digits(integer = 8, fraction = 2) BigDecimal price,
		@NotNull @Min(0) Integer stock,
		@Size(max = 2000) String description
) {
}
