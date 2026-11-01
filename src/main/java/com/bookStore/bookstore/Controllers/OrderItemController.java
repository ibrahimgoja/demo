package com.bookStore.bookstore.Controllers;

import com.bookStore.bookstore.Models.dto.OrderItemDto;
import com.bookStore.bookstore.Services.OrderItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

	private final OrderItemService orderItemService;

	public OrderItemController(OrderItemService orderItemService) {
		this.orderItemService = orderItemService;
	}

	@GetMapping
	public List<OrderItemDto> getAllOrderItems() {
		return orderItemService.findAll();
	}

	@GetMapping("/order/{orderId}")
	public List<OrderItemDto> getOrderItemsByOrderId(@PathVariable UUID orderId) {
		return orderItemService.findByOrderId(orderId);
	}

	@GetMapping("/{id}")
	public OrderItemDto getOrderItemById(@PathVariable UUID id) {
		return orderItemService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public OrderItemDto createOrderItem(@Valid @RequestBody OrderItemDto dto) {
		return orderItemService.create(dto);
	}

	@PutMapping("/{id}")
	public OrderItemDto updateOrderItem(@PathVariable UUID id, @Valid @RequestBody OrderItemDto dto) {
		return orderItemService.update(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteOrderItem(@PathVariable UUID id) {
		orderItemService.delete(id);
	}
}
