package com.bookStore.bookstore.repositories;

import com.bookStore.bookstore.Models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

	List<CartItem> findByUser_Id(UUID userId);

	Optional<CartItem> findByUser_IdAndBook_Id(UUID userId, UUID bookId);
}
