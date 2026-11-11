package com.bookStore.bookstore.openapi.support;

import com.bookStore.bookstore.Models.dto.request.BookRequest;
import com.bookStore.bookstore.Models.dto.response.BookResponse;
import com.bookStore.bookstore.openapi.model.PageBookResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookOpenApiMapper {

	public BookRequest toInternalRequest(com.bookStore.bookstore.openapi.model.BookRequest request) {
		return new BookRequest(
				request.getTitle(),
				request.getAuthor(),
				request.getIsbn(),
				request.getPrice(),
				request.getStock(),
				request.getDescription());
	}

	public com.bookStore.bookstore.openapi.model.BookResponse toOpenApiResponse(BookResponse response) {
		com.bookStore.bookstore.openapi.model.BookResponse api = new com.bookStore.bookstore.openapi.model.BookResponse();
		api.setId(response.id());
		api.setTitle(response.title());
		api.setAuthor(response.author());
		api.setIsbn(response.isbn());
		api.setPrice(response.price());
		api.setStock(response.stock());
		api.setDescription(response.description());
		return api;
	}

	public PageBookResponse toPageBookResponse(Page<BookResponse> page) {
		PageBookResponse result = new PageBookResponse();
		result.setContent(page.getContent().stream().map(this::toOpenApiResponse).collect(Collectors.toList()));
		result.setTotalElements(page.getTotalElements());
		result.setTotalPages(page.getTotalPages());
		result.setSize(page.getSize());
		result.setNumber(page.getNumber());
		result.setFirst(page.isFirst());
		result.setLast(page.isLast());
		result.setEmpty(page.isEmpty());
		result.setNumberOfElements(page.getNumberOfElements());
		return result;
	}
}
