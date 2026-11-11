package com.bookStore.bookstore.openapi.support;

import com.bookStore.bookstore.Services.BookService;
import com.bookStore.bookstore.openapi.api.BooksApiDelegate;
import com.bookStore.bookstore.openapi.model.BookRequest;
import com.bookStore.bookstore.openapi.model.BookResponse;
import com.bookStore.bookstore.openapi.model.PageBookResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BooksApiDelegateImpl implements BooksApiDelegate {

	private final BookService bookService;
	private final BookOpenApiMapper mapper;

	public BooksApiDelegateImpl(BookService bookService, BookOpenApiMapper mapper) {
		this.bookService = bookService;
		this.mapper = mapper;
	}

	@Override
	public ResponseEntity<PageBookResponse> listBooks(String q, Integer page, Integer size, String sort) {
		Pageable pageable = toPageable(page, size, sort);
		PageBookResponse body = mapper.toPageBookResponse(bookService.findAll(q, pageable));
		return ResponseEntity.ok(body);
	}

	@Override
	public ResponseEntity<BookResponse> getBookById(UUID id) {
		return ResponseEntity.ok(mapper.toOpenApiResponse(bookService.findById(id)));
	}

	@Override
	public ResponseEntity<BookResponse> createBook(BookRequest bookRequest) {
		BookResponse body = mapper.toOpenApiResponse(
				bookService.create(mapper.toInternalRequest(bookRequest)));
		return ResponseEntity.status(HttpStatus.CREATED).body(body);
	}

	@Override
	public ResponseEntity<BookResponse> updateBook(UUID id, BookRequest bookRequest) {
		BookResponse body = mapper.toOpenApiResponse(
				bookService.update(id, mapper.toInternalRequest(bookRequest)));
		return ResponseEntity.ok(body);
	}

	@Override
	public ResponseEntity<Void> deleteBook(UUID id) {
		bookService.delete(id);
		return ResponseEntity.noContent().build();
	}

	private static Pageable toPageable(Integer page, Integer size, String sort) {
		int pageIndex = page != null ? page : 0;
		int pageSize = size != null ? size : 10;
		Sort sortSpec = parseSort(sort);
		return PageRequest.of(pageIndex, pageSize, sortSpec);
	}

	private static Sort parseSort(String sort) {
		if (sort == null || sort.isBlank()) {
			return Sort.by(Sort.Direction.ASC, "title");
		}
		String[] parts = sort.split(",", 2);
		String property = parts[0].trim();
		if (parts.length == 1 || parts[1].isBlank()) {
			return Sort.by(property);
		}
		Sort.Direction direction = "desc".equalsIgnoreCase(parts[1].trim())
				? Sort.Direction.DESC
				: Sort.Direction.ASC;
		return Sort.by(direction, property);
	}
}
