package com.bookStore.bookstore.Models.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
		@NotBlank @Email @Size(max = 255) String email,
		@NotBlank @Size(min = 1, max = 100) String username,
		@Size(min = 4, max = 100) String password
) {
}
