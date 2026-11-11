package com.bookStore.bookstore.repositories;

import com.bookStore.bookstore.Models.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, UUID> {

	List<OrderItemEntity> findByOrder_Id(UUID orderId);
}
