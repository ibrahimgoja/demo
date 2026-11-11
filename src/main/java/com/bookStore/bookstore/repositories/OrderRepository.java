package com.bookStore.bookstore.repositories;

import com.bookStore.bookstore.Models.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

	List<OrderEntity> findByUser_Id(UUID userId);
}
