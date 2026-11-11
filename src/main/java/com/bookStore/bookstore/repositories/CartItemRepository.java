package com.bookStore.bookstore.repositories;

import com.bookStore.bookstore.Models.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {

	List<CartItemEntity> findByUser_Id(UUID userId);

	Optional<CartItemEntity> findByUser_IdAndBook_Id(UUID userId, UUID bookId);
}
