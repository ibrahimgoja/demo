package com.bookStore.bookstore.Services;

import com.bookStore.bookstore.Models.dto.request.OrderItemRequest;
import com.bookStore.bookstore.Models.dto.response.OrderItemResponse;
import com.bookStore.bookstore.Models.entity.BookEntity;
import com.bookStore.bookstore.Models.entity.OrderEntity;
import com.bookStore.bookstore.Models.entity.OrderItemEntity;
import com.bookStore.bookstore.Models.mappers.OrderItemMapper;
import com.bookStore.bookstore.repositories.BookRepository;
import com.bookStore.bookstore.repositories.OrderItemRepository;
import com.bookStore.bookstore.repositories.OrderRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Validated
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

	public List<OrderItemResponse> findAll() {
		return orderItemRepository.findAll().stream().map(orderItemMapper::toResponse).toList();
	}

	public List<OrderItemResponse> findByOrderId(UUID orderId) {
		if (!orderRepository.existsById(orderId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + orderId);
		}
		return orderItemRepository.findByOrder_Id(orderId).stream().map(orderItemMapper::toResponse).toList();
	}

	public OrderItemResponse findById(UUID id) {
		OrderItemEntity item = orderItemRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found with id: " + id));
		return orderItemMapper.toResponse(item);
	}

	@Transactional
	public OrderItemResponse create(@Valid OrderItemRequest request) {
		OrderEntity order = orderRepository.findById(request.orderId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found with id: " + request.orderId()));
		BookEntity book = bookRepository.findById(request.bookId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + request.bookId()));

		OrderItemEntity saved = orderItemRepository.save(OrderItemEntity.builder()
				.order(order)
				.book(book)
				.quantity(request.quantity())
				.unitPrice(request.unitPrice())
				.build());
		return orderItemMapper.toResponse(saved);
	}

	@Transactional
	public OrderItemResponse update(UUID id, @Valid OrderItemRequest request) {
		OrderItemEntity item = orderItemRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found with id: " + id));
		BookEntity book = bookRepository.findById(request.bookId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + request.bookId()));

		item.setBook(book);
		item.setQuantity(request.quantity());
		item.setUnitPrice(request.unitPrice());
		return orderItemMapper.toResponse(item);
	}

	@Transactional
	public void delete(UUID id) {
		if (!orderItemRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order item not found with id: " + id);
		}
		orderItemRepository.deleteById(id);
	}
}
