package com.bookStore.bookstore.Services;

import com.bookStore.bookstore.Models.dto.request.BookRequest;
import com.bookStore.bookstore.Models.dto.response.BookResponse;
import com.bookStore.bookstore.Models.entity.BookEntity;
import com.bookStore.bookstore.Models.mappers.BookMapper;
import com.bookStore.bookstore.Specifications.BookSearchSpecifications;
import com.bookStore.bookstore.repositories.BookRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Validated
@Transactional(readOnly = true)
public class BookService {

	private final BookRepository bookRepository;
	private final BookMapper bookMapper;

	public BookService(BookRepository bookRepository, BookMapper bookMapper) {
		this.bookRepository = bookRepository;
		this.bookMapper = bookMapper;
	}

	public Page<BookResponse> findAll(String keyword, Pageable pageable) {
		return bookRepository
				.findAll(BookSearchSpecifications.titleContains(keyword), pageable)
				.map(bookMapper::toResponse);
	}

	public BookResponse findById(UUID id) {
		BookEntity book = bookRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Book not found with id: " + id));
		return bookMapper.toResponse(book);
	}

	@Transactional
	public BookResponse create(@Valid BookRequest request) {
		if (bookRepository.existsByIsbn(request.isbn())) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "A book with ISBN " + request.isbn() + " already exists");
		}
		try {
			BookEntity saved = bookRepository.save(bookMapper.toEntity(request));
			return bookMapper.toResponse(saved);
		} catch (DataIntegrityViolationException ex) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "ISBN may already be in use");
		}
	}

	@Transactional
	public BookResponse update(UUID id, @Valid BookRequest request) {
		BookEntity book = bookRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Book not found with id: " + id));
		if (bookRepository.existsByIsbnAndIdNot(request.isbn(), id)) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "A book with ISBN " + request.isbn() + " already exists");
		}
		bookMapper.applyUpdate(book, request);
		return bookMapper.toResponse(book);
	}

	@Transactional
	public void delete(UUID id) {
		if (!bookRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found with id: " + id);
		}
		bookRepository.deleteById(id);
	}
}
