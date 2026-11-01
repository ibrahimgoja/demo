package com.bookStore.bookstore.Controllers;

import com.bookStore.bookstore.Services.BookService;
import com.bookStore.bookstore.Models.dto.BookDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@GetMapping
	public Page<BookDto> getAllBooks(
			@RequestParam(required = false) String q,
			@PageableDefault(size = 10, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
		return bookService.findAll(q, pageable);
	}

	@GetMapping("/{id}")
	public BookDto getBookById(@PathVariable UUID id) {
		return bookService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDto createBook(@Valid @RequestBody BookDto dto) {
		return bookService.create(dto);
	}

	@PutMapping("/{id}")
	public BookDto updateBook(@PathVariable UUID id, @Valid @RequestBody BookDto dto) {
		return bookService.update(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteBook(@PathVariable UUID id) {
		bookService.delete(id);
	}
}
