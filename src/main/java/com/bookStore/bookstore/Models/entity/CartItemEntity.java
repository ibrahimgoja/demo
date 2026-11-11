package com.bookStore.bookstore.Models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(
		name = "cart_items",
		uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "book_id"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@ManyToOne(optional = false)
	@JoinColumn(name = "book_id")
	private BookEntity book;

	@Column(nullable = false)
	private Integer quantity;
}
