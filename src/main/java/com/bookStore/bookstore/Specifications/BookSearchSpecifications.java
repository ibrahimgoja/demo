package com.bookStore.bookstore.Specifications;

import com.bookStore.bookstore.Models.entity.BookEntity;
import org.springframework.data.jpa.domain.Specification;

public final class BookSearchSpecifications {

	private BookSearchSpecifications() {
	}

	public static Specification<BookEntity> titleContains(String keyword) {
		return (root, query, cb) -> {
			if (keyword == null || keyword.isBlank()) {
				return cb.conjunction();
			}
			String pattern = "%" + keyword.trim().toLowerCase() + "%";
			return cb.like(cb.lower(root.get("title")), pattern);
		};
	}
}
