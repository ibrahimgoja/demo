package com.bookStore.bookstore.Models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

	private UUID id;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Size(max = 100)
	private String username;

	@Size(min = 4, max = 100)
	private String password;
}
