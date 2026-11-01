package com.bookStore.bookstore.Controllers;

import com.bookStore.bookstore.Models.dto.OrderDto;
import com.bookStore.bookstore.Services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@GetMapping
	public List<OrderDto> getAllOrders() {
		return orderService.findAll();
	}

	@GetMapping("/user/{userId}")
	public List<OrderDto> getOrdersByUserId(@PathVariable UUID userId) {
		return orderService.findByUserId(userId);
	}

	@GetMapping("/{id}")
	public OrderDto getOrderById(@PathVariable UUID id) {
		return orderService.findById(id);
	}

	@PostMapping("/checkout/{userId}")
	@ResponseStatus(HttpStatus.CREATED)
	public OrderDto checkout(@PathVariable UUID userId) {
		return orderService.checkout(userId);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteOrder(@PathVariable UUID id) {
		orderService.delete(id);
	}
}
