package com.bookStore.bookstore.repositories;

import com.bookStore.bookstore.Models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>, JpaSpecificationExecutor<Book> {

	boolean existsByIsbn(String isbn);

	boolean existsByIsbnAndIdNot(String isbn, UUID id);
}
