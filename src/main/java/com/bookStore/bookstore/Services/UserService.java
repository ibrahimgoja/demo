package com.bookStore.bookstore.Services;

import com.bookStore.bookstore.Models.dto.request.UserRequest;
import com.bookStore.bookstore.Models.dto.response.UserResponse;
import com.bookStore.bookstore.Models.entity.UserEntity;
import com.bookStore.bookstore.Models.mappers.UserMapper;
import com.bookStore.bookstore.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Validated
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public UserService(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	public List<UserResponse> findAll() {
		return userRepository.findAll().stream().map(userMapper::toResponse).toList();
	}

	public UserResponse findById(UUID id) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
		return userMapper.toResponse(user);
	}

	@Transactional
	public UserResponse create(@Valid UserRequest request) {
		if (request.password() == null || request.password().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
		}
		if (userRepository.existsByEmail(request.email())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
		}
		if (userRepository.existsByUsername(request.username())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use");
		}
		UserEntity saved = userRepository.save(userMapper.toEntity(request));
		return userMapper.toResponse(saved);
	}

	@Transactional
	public UserResponse update(UUID id, @Valid UserRequest request) {
		UserEntity user = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
		if (userRepository.existsByEmailAndIdNot(request.email(), id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
		}
		if (userRepository.existsByUsernameAndIdNot(request.username(), id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use");
		}
		user.setEmail(request.email());
		user.setUsername(request.username());
		if (request.password() != null && !request.password().isBlank()) {
			user.setPassword(request.password());
		}
		return userMapper.toResponse(user);
	}

	@Transactional
	public void delete(UUID id) {
		if (!userRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
		}
		userRepository.deleteById(id);
	}
}
