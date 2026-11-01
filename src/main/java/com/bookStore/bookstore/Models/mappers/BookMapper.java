package com.bookStore.bookstore.Models.mappers;

import com.bookStore.bookstore.Models.Book;
import com.bookStore.bookstore.Models.dto.BookDto;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

	public Book toEntity(BookDto dto) {
		return Book.builder()
				.title(dto.getTitle())
				.author(dto.getAuthor())
				.isbn(dto.getIsbn())
				.price(dto.getPrice())
				.stock(dto.getStock())
				.description(dto.getDescription())
				.build();
	}

	public BookDto toDto(Book book) {
		return BookDto.builder()
				.id(book.getId())
				.title(book.getTitle())
				.author(book.getAuthor())
				.isbn(book.getIsbn())
				.price(book.getPrice())
				.stock(book.getStock())
				.description(book.getDescription())
				.build();
	}
}
