package com.bookStore.bookstore.Models.mappers;

import com.bookStore.bookstore.Models.dto.response.OrderResponse;
import com.bookStore.bookstore.Models.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

	public OrderResponse toResponse(OrderEntity order) {
		return new OrderResponse(
				order.getId(),
				order.getUser().getId(),
				order.getOrderedAt(),
				order.getTotalPrice());
	}
}
