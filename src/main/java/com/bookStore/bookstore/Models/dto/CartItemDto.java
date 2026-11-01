package com.bookStore.bookstore.Models.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

	private UUID id;

	@NotNull
	private UUID userId;

	@NotNull
	private UUID bookId;

	@NotNull
	@Min(1)
	private Integer quantity;
}
