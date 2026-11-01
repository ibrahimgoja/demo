package com.bookStore.bookstore.Services;

import com.bookStore.bookstore.Models.CartItem;
import com.bookStore.bookstore.Models.Order;
import com.bookStore.bookstore.Models.OrderItem;
import com.bookStore.bookstore.Models.User;
import com.bookStore.bookstore.Models.dto.OrderDto;
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

	public List<OrderDto> findAll() {
		return orderRepository.findAll().stream().map(orderMapper::toDto).toList();
	}

	public List<OrderDto> findByUserId(UUID userId) {
		if (!userRepository.existsById(userId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
		}
		return orderRepository.findByUser_Id(userId).stream().map(orderMapper::toDto).toList();
	}

	public OrderDto findById(UUID id) {
		Order order = orderRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + id));
		return orderMapper.toDto(order);
	}

	@Transactional
	public OrderDto checkout(UUID userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId));

		List<CartItem> cartItems = cartItemRepository.findByUser_Id(userId);
		if (cartItems.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
		}

		BigDecimal total = BigDecimal.ZERO;
		Order order = Order.builder()
				.user(user)
				.orderedAt(LocalDateTime.now())
				.totalPrice(BigDecimal.ZERO)
				.build();

		for (CartItem cartItem : cartItems) {
			if (cartItem.getBook().getStock() < cartItem.getQuantity()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Not enough stock for book: " + cartItem.getBook().getId());
			}
			BigDecimal lineTotal = cartItem.getBook().getPrice()
					.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
			total = total.add(lineTotal);

			OrderItem orderItem = OrderItem.builder()
					.order(order)
					.book(cartItem.getBook())
					.quantity(cartItem.getQuantity())
					.unitPrice(cartItem.getBook().getPrice())
					.build();
			order.getItems().add(orderItem);

			cartItem.getBook().setStock(cartItem.getBook().getStock() - cartItem.getQuantity());
		}

		order.setTotalPrice(total);
		Order saved = orderRepository.save(order);
		cartItemRepository.deleteAll(cartItems);

		return orderMapper.toDto(saved);
	}

	@Transactional
	public void delete(UUID id) {
		if (!orderRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + id);
		}
		orderRepository.deleteById(id);
	}
}
