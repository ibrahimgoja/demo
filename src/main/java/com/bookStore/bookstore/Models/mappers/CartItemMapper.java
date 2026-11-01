package com.bookStore.bookstore.Models.mappers;

import com.bookStore.bookstore.Models.CartItem;
import com.bookStore.bookstore.Models.dto.CartItemDto;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

	public CartItemDto toDto(CartItem item) {
		return CartItemDto.builder()
				.id(item.getId())
				.userId(item.getUser().getId())
				.bookId(item.getBook().getId())
				.quantity(item.getQuantity())
				.build();
	}
}
