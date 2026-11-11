package com.bookStore.bookstore.Services;

import com.bookStore.bookstore.Models.dto.request.CartItemRequest;
import com.bookStore.bookstore.Models.dto.response.CartItemResponse;
import com.bookStore.bookstore.Models.entity.BookEntity;
import com.bookStore.bookstore.Models.entity.CartItemEntity;
import com.bookStore.bookstore.Models.entity.UserEntity;
import com.bookStore.bookstore.Models.mappers.CartItemMapper;
import com.bookStore.bookstore.repositories.BookRepository;
import com.bookStore.bookstore.repositories.CartItemRepository;
import com.bookStore.bookstore.repositories.UserRepository;
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
public class CartItemService {

	private final CartItemRepository cartItemRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final CartItemMapper cartItemMapper;

	public CartItemService(
			CartItemRepository cartItemRepository,
			UserRepository userRepository,
			BookRepository bookRepository,
			CartItemMapper cartItemMapper) {
		this.cartItemRepository = cartItemRepository;
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
		this.cartItemMapper = cartItemMapper;
	}

	public List<CartItemResponse> findAll() {
		return cartItemRepository.findAll().stream().map(cartItemMapper::toResponse).toList();
	}

	public List<CartItemResponse> findByUserId(UUID userId) {
		if (!userRepository.existsById(userId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
		}
		return cartItemRepository.findByUser_Id(userId).stream().map(cartItemMapper::toResponse).toList();
	}

	public CartItemResponse findById(UUID id) {
		CartItemEntity item = cartItemRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found with id: " + id));
		return cartItemMapper.toResponse(item);
	}

	@Transactional
	public CartItemResponse create(@Valid CartItemRequest request) {
		UserEntity user = userRepository.findById(request.userId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + request.userId()));
		BookEntity book = bookRepository.findById(request.bookId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + request.bookId()));
		if (book.getStock() < request.quantity()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock for book: " + book.getId());
		}

		return cartItemRepository.findByUser_IdAndBook_Id(request.userId(), request.bookId())
				.map(existing -> {
					existing.setQuantity(existing.getQuantity() + request.quantity());
					return cartItemMapper.toResponse(existing);
				})
				.orElseGet(() -> {
					CartItemEntity saved = cartItemRepository.save(CartItemEntity.builder()
							.user(user)
							.book(book)
							.quantity(request.quantity())
							.build());
					return cartItemMapper.toResponse(saved);
				});
	}

	@Transactional
	public CartItemResponse update(UUID id, @Valid CartItemRequest request) {
		CartItemEntity item = cartItemRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found with id: " + id));
		BookEntity book = bookRepository.findById(request.bookId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + request.bookId()));
		if (book.getStock() < request.quantity()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock for book: " + book.getId());
		}
		item.setQuantity(request.quantity());
		item.setBook(book);
		return cartItemMapper.toResponse(item);
	}

	@Transactional
	public void delete(UUID id) {
		if (!cartItemRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found with id: " + id);
		}
		cartItemRepository.deleteById(id);
	}
}
