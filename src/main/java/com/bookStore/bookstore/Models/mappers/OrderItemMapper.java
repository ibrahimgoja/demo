package com.bookStore.bookstore.Models.mappers;

import com.bookStore.bookstore.Models.dto.response.OrderItemResponse;
import com.bookStore.bookstore.Models.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

	public OrderItemResponse toResponse(OrderItemEntity item) {
		return new OrderItemResponse(
				item.getId(),
				item.getOrder().getId(),
				item.getBook().getId(),
				item.getQuantity(),
				item.getUnitPrice());
	}
}
