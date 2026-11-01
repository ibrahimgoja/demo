package com.bookStore.bookstore.Models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

	private UUID id;

	@NotNull
	private UUID userId;

	private LocalDateTime orderedAt;

	private BigDecimal totalPrice;
}
