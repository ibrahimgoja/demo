package com.bookStore.bookstore.Services;

import com.bookStore.bookstore.Models.Book;
import com.bookStore.bookstore.Models.dto.BookDto;
import com.bookStore.bookstore.Models.mappers.BookMapper;
import com.bookStore.bookstore.Specifications.BookSpecification;
import com.bookStore.bookstore.repositories.BookRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class BookService {

	private final BookRepository bookRepository;
	private final BookMapper bookMapper;

	public BookService(BookRepository bookRepository, BookMapper bookMapper) {
		this.bookRepository = bookRepository;
		this.bookMapper = bookMapper;
	}

	public Page<BookDto> findAll(String keyword, Pageable pageable) {
		return bookRepository
				.findAll(BookSpecification.searchByTitle(keyword), pageable)
				.map(bookMapper::toDto);
	}

	public BookDto findById(UUID id) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Book not found with id: " + id));
		return bookMapper.toDto(book);
	}

	@Transactional
	public BookDto create(BookDto dto) {
		if (bookRepository.existsByIsbn(dto.getIsbn())) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "A book with ISBN " + dto.getIsbn() + " already exists");
		}
		try {
			Book saved = bookRepository.save(bookMapper.toEntity(dto));
			return bookMapper.toDto(saved);
		} catch (DataIntegrityViolationException ex) {
			throw new ResponseStatusException(
					HttpStatus.CONFLICT, "ISBN may already be in use");
		}
	}

	@Transactional
	public BookDto update(UUID id, BookDto dto) {
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(
						HttpStatus.NOT_FOUND, "Book not found with id: " + id));
		if (bookRepository.existsByIsbnAndIdNot(dto.getIsbn(), id)) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "A book with ISBN " + dto.getIsbn() + " already exists");
		}
		book.setTitle(dto.getTitle());
		book.setAuthor(dto.getAuthor());
		book.setIsbn(dto.getIsbn());
		book.setPrice(dto.getPrice());
		book.setStock(dto.getStock());
		book.setDescription(dto.getDescription());
		return bookMapper.toDto(book);
	}

	@Transactional
	public void delete(UUID id) {
		if (!bookRepository.existsById(id)) {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Book not found with id: " + id);
		}
		bookRepository.deleteById(id);
	}
}
