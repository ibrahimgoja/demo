package com.bookStore.bookstore.Models.dto.response;

import java.util.UUID;

public record UserResponse(
		UUID id,
		String email,
		String username
) {
}
