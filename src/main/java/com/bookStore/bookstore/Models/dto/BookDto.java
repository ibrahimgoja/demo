package com.bookStore.bookstore.Models.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {

	private UUID id;

	private String title;

	private String author;

	private String isbn;

	private BigDecimal price;

	private Integer stock;

	private String description;
}
