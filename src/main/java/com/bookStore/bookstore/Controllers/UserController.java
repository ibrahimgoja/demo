package com.bookStore.bookstore.Controllers;

import com.bookStore.bookstore.Models.dto.UserDto;
import com.bookStore.bookstore.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public List<UserDto> getAllUsers() {
		return userService.findAll();
	}

	@GetMapping("/{id}")
	public UserDto getUserById(@PathVariable UUID id) {
		return userService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto createUser(@Valid @RequestBody UserDto dto) {
		return userService.create(dto);
	}

	@PutMapping("/{id}")
	public UserDto updateUser(@PathVariable UUID id, @Valid @RequestBody UserDto dto) {
		return userService.update(id, dto);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable UUID id) {
		userService.delete(id);
	}
}
