package com.bookStore.bookstore.Models.mappers;

import com.bookStore.bookstore.Models.User;
import com.bookStore.bookstore.Models.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

	public User toEntity(UserDto dto) {
		return User.builder()
				.email(dto.getEmail())
				.username(dto.getUsername())
				.password(dto.getPassword())
				.build();
	}

	public UserDto toDto(User user) {
		return UserDto.builder()
				.id(user.getId())
				.email(user.getEmail())
				.username(user.getUsername())
				.build();
	}
}
