package com.bookStore.bookstore.Models.mappers;

import com.bookStore.bookstore.Models.dto.response.CartItemResponse;
import com.bookStore.bookstore.Models.entity.CartItemEntity;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

	public CartItemResponse toResponse(CartItemEntity item) {
		return new CartItemResponse(
				item.getId(),
				item.getUser().getId(),
				item.getBook().getId(),
				item.getQuantity());
	}
}
