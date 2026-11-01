package com.bookStore.bookstore.Models.mappers;

import com.bookStore.bookstore.Models.OrderItem;
import com.bookStore.bookstore.Models.dto.OrderItemDto;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

	public OrderItemDto toDto(OrderItem item) {
		return OrderItemDto.builder()
				.id(item.getId())
				.orderId(item.getOrder().getId())
				.bookId(item.getBook().getId())
				.quantity(item.getQuantity())
				.unitPrice(item.getUnitPrice())
				.build();
	}
}
