package com.bookStore.bookstore.Services;

import com.bookStore.bookstore.Models.dto.response.OrderResponse;
import com.bookStore.bookstore.Models.entity.CartItemEntity;
import com.bookStore.bookstore.Models.entity.OrderEntity;
import com.bookStore.bookstore.Models.entity.OrderItemEntity;
import com.bookStore.bookstore.Models.entity.UserEntity;
import com.bookStore.bookstore.Models.mappers.OrderMapper;
import com.bookStore.bookstore.repositories.CartItemRepository;
import com.bookStore.bookstore.repositories.OrderRepository;
import com.bookStore.bookstore.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;
	private final CartItemRepository cartItemRepository;
	private final OrderMapper orderMapper;

	public OrderService(
			OrderRepository orderRepository,
			UserRepository userRepository,
			CartItemRepository cartItemRepository,
			OrderMapper orderMapper) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.cartItemRepository = cartItemRepository;
		this.orderMapper = orderMapper;
	}

	public List<OrderResponse> findAll() {
		return orderRepository.findAll().stream().map(orderMapper::toResponse).toList();
	}

	public List<OrderResponse> findByUserId(UUID userId) {
		if (!userRepository.existsById(userId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
		}
		return orderRepository.findByUser_Id(userId).stream().map(orderMapper::toResponse).toList();
	}

	public OrderResponse findById(UUID id) {
		OrderEntity order = orderRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + id));
		return orderMapper.toResponse(order);
	}

	@Transactional
	public OrderResponse checkout(UUID userId) {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

		List<CartItemEntity> cartItems = cartItemRepository.findByUser_Id(userId);
		if (cartItems.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
		}

		BigDecimal total = BigDecimal.ZERO;
		OrderEntity order = OrderEntity.builder()
				.user(user)
				.orderedAt(LocalDateTime.now())
				.totalPrice(BigDecimal.ZERO)
				.build();

		for (CartItemEntity cartItem : cartItems) {
			if (cartItem.getBook().getStock() < cartItem.getQuantity()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Not enough stock for book: " + cartItem.getBook().getId());
			}
			BigDecimal lineTotal = cartItem.getBook().getPrice()
					.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
			total = total.add(lineTotal);

			OrderItemEntity orderItem = OrderItemEntity.builder()
					.order(order)
					.book(cartItem.getBook())
					.quantity(cartItem.getQuantity())
					.unitPrice(cartItem.getBook().getPrice())
					.build();
			order.getItems().add(orderItem);

			cartItem.getBook().setStock(cartItem.getBook().getStock() - cartItem.getQuantity());
		}

		order.setTotalPrice(total);
		OrderEntity saved = orderRepository.save(order);
		cartItemRepository.deleteAll(cartItems);

		return orderMapper.toResponse(saved);
	}

	@Transactional
	public void delete(UUID id) {
		if (!orderRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + id);
		}
		orderRepository.deleteById(id);
	}
}
