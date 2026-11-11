package com.bookStore.bookstore.repositories;

import com.bookStore.bookstore.Models.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

	boolean existsByEmail(String email);

	boolean existsByUsername(String username);

	boolean existsByEmailAndIdNot(String email, UUID id);

	boolean existsByUsernameAndIdNot(String username, UUID id);
}
