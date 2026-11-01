package com.bookStore.bookstore.Models.mappers;

import com.bookStore.bookstore.Models.Order;
import com.bookStore.bookstore.Models.dto.OrderDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

	public OrderDto toDto(Order order) {
		return OrderDto.builder()
				.id(order.getId())
				.userId(order.getUser().getId())
				.orderedAt(order.getOrderedAt())
				.totalPrice(order.getTotalPrice())
				.build();
	}
}
