package com.bookStore.bookstore.Services;

import com.bookStore.bookstore.Models.Book;
import com.bookStore.bookstore.Models.CartItem;
import com.bookStore.bookstore.Models.User;
import com.bookStore.bookstore.Models.dto.CartItemDto;
import com.bookStore.bookstore.Models.mappers.CartItemMapper;
import com.bookStore.bookstore.repositories.BookRepository;
import com.bookStore.bookstore.repositories.CartItemRepository;
import com.bookStore.bookstore.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
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

	public List<CartItemDto> findAll() {
		return cartItemRepository.findAll().stream().map(cartItemMapper::toDto).toList();
	}

	public List<CartItemDto> findByUserId(UUID userId) {
		if (!userRepository.existsById(userId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + userId);
		}
		return cartItemRepository.findByUser_Id(userId).stream().map(cartItemMapper::toDto).toList();
	}

	public CartItemDto findById(UUID id) {
		CartItem item = cartItemRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found with id: " + id));
		return cartItemMapper.toDto(item);
	}

	@Transactional
	public CartItemDto create(CartItemDto dto) {
		User user = userRepository.findById(dto.getUserId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + dto.getUserId()));
		Book book = bookRepository.findById(dto.getBookId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + dto.getBookId()));
		if (book.getStock() < dto.getQuantity()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock for book: " + book.getId());
		}

		return cartItemRepository.findByUser_IdAndBook_Id(dto.getUserId(), dto.getBookId())
				.map(existing -> {
					existing.setQuantity(existing.getQuantity() + dto.getQuantity());
					return cartItemMapper.toDto(existing);
				})
				.orElseGet(() -> {
					CartItem saved = cartItemRepository.save(CartItem.builder()
							.user(user)
							.book(book)
							.quantity(dto.getQuantity())
							.build());
					return cartItemMapper.toDto(saved);
				});
	}

	@Transactional
	public CartItemDto update(UUID id, CartItemDto dto) {
		CartItem item = cartItemRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found with id: " + id));
		Book book = bookRepository.findById(dto.getBookId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + dto.getBookId()));
		if (book.getStock() < dto.getQuantity()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock for book: " + book.getId());
		}
		item.setQuantity(dto.getQuantity());
		item.setBook(book);
		return cartItemMapper.toDto(item);
	}

	@Transactional
	public void delete(UUID id) {
		if (!cartItemRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found with id: " + id);
		}
		cartItemRepository.deleteById(id);
	}
}
