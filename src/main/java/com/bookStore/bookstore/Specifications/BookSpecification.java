package com.bookStore.bookstore.Specifications;

import com.bookStore.bookstore.Models.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

	private BookSpecification() {
	}

	public static Specification<Book> searchByTitle(String keyword) {
		return (root, query, cb) -> {
			if (keyword == null || keyword.isBlank()) {
				return cb.conjunction();
			}
			String pattern = "%" + keyword.trim().toLowerCase() + "%";
			return cb.like(cb.lower(root.get("title")), pattern);
		};
	}
}
