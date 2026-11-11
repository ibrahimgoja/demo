package com.bookStore.bookstore.Models.mappers;

import com.bookStore.bookstore.Models.dto.request.UserRequest;
import com.bookStore.bookstore.Models.dto.response.UserResponse;
import com.bookStore.bookstore.Models.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

	public UserEntity toEntity(UserRequest request) {
		return UserEntity.builder()
				.email(request.email())
				.username(request.username())
				.password(request.password())
				.build();
	}

	public UserResponse toResponse(UserEntity user) {
		return new UserResponse(user.getId(), user.getEmail(), user.getUsername());
	}
}
