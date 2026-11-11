package com.bookStore.bookstore.Models.mappers;

import com.bookStore.bookstore.Models.dto.request.BookRequest;
import com.bookStore.bookstore.Models.dto.response.BookResponse;
import com.bookStore.bookstore.Models.entity.BookEntity;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

	public BookEntity toEntity(BookRequest request) {
		return BookEntity.builder()
				.title(request.title())
				.author(request.author())
				.isbn(request.isbn())
				.price(request.price())
				.stock(request.stock())
				.description(request.description())
				.build();
	}

	public BookResponse toResponse(BookEntity book) {
		return new BookResponse(
				book.getId(),
				book.getTitle(),
				book.getAuthor(),
				book.getIsbn(),
				book.getPrice(),
				book.getStock(),
				book.getDescription());
	}

	public void applyUpdate(BookEntity book, BookRequest request) {
		book.setTitle(request.title());
		book.setAuthor(request.author());
		book.setIsbn(request.isbn());
		book.setPrice(request.price());
		book.setStock(request.stock());
		book.setDescription(request.description());
	}
}
