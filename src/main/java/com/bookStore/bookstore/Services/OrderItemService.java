package com.bookStore.bookstore.Services;

import com.bookStore.bookstore.Models.Book;
import com.bookStore.bookstore.Models.Order;
import com.bookStore.bookstore.Models.OrderItem;
import com.bookStore.bookstore.Models.dto.OrderItemDto;
import com.bookStore.bookstore.Models.mappers.OrderItemMapper;
import com.bookStore.bookstore.repositories.BookRepository;
import com.bookStore.bookstore.repositories.OrderItemRepository;
import com.bookStore.bookstore.repositories.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class OrderItemService {

	private final OrderItemRepository orderItemRepository;
	private final OrderRepository orderRepository;
	private final BookRepository bookRepository;
	private final OrderItemMapper orderItemMapper;

	public OrderItemService(
			OrderItemRepository orderItemRepository,
			OrderRepository orderRepository,
			BookRepository bookRepository,
			OrderItemMapper orderItemMapper) {
		this.orderItemRepository = orderItemRepository;
		this.orderRepository = orderRepository;
		this.bookRepository = bookRepository;
		this.orderItemMapper = orderItemMapper;
	}

	public List<OrderItemDto> findAll() {
		return orderItemRepository.findAll().stream().map(orderItemMapper::toDto).toList();
	}

	public List<OrderItemDto> findByOrderId(UUID orderId) {
		if (!orderRepository.existsById(orderId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + orderId);
		}
		return orderItemRepository.findByOrder_Id(orderId).stream().map(orderItemMapper::toDto).toList();
	}

	public OrderItemDto findById(UUID id) {
		OrderItem item = orderItemRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found with id: " + id));
		return orderItemMapper.toDto(item);
	}

	@Transactional
	public OrderItemDto create(OrderItemDto dto) {
		Order order = orderRepository.findById(dto.getOrderId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + dto.getOrderId()));
		Book book = bookRepository.findById(dto.getBookId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + dto.getBookId()));

		OrderItem saved = orderItemRepository.save(OrderItem.builder()
				.order(order)
				.book(book)
				.quantity(dto.getQuantity())
				.unitPrice(dto.getUnitPrice())
				.build());
		return orderItemMapper.toDto(saved);
	}

	@Transactional
	public OrderItemDto update(UUID id, OrderItemDto dto) {
		OrderItem item = orderItemRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found with id: " + id));
		Book book = bookRepository.findById(dto.getBookId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + dto.getBookId()));

		item.setBook(book);
		item.setQuantity(dto.getQuantity());
		item.setUnitPrice(dto.getUnitPrice());
		return orderItemMapper.toDto(item);
	}

	@Transactional
	public void delete(UUID id) {
		if (!orderItemRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found with id: " + id);
		}
		orderItemRepository.deleteById(id);
	}
}
