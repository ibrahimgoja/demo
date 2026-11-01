package com.bookStore.bookstore.Services;

import com.bookStore.bookstore.Models.User;
import com.bookStore.bookstore.Models.dto.UserDto;
import com.bookStore.bookstore.Models.mappers.UserMapper;
import com.bookStore.bookstore.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public UserService(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	public List<UserDto> findAll() {
		return userRepository.findAll().stream().map(userMapper::toDto).toList();
	}

	public UserDto findById(UUID id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
		return userMapper.toDto(user);
	}

	@Transactional
	public UserDto create(UserDto dto) {
		if (dto.getPassword() == null || dto.getPassword().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
		}
		if (userRepository.existsByEmail(dto.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
		}
		if (userRepository.existsByUsername(dto.getUsername())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use");
		}
		User saved = userRepository.save(userMapper.toEntity(dto));
		return userMapper.toDto(saved);
	}

	@Transactional
	public UserDto update(UUID id, UserDto dto) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id));
		if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
		}
		if (userRepository.existsByUsernameAndIdNot(dto.getUsername(), id)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use");
		}
		user.setEmail(dto.getEmail());
		user.setUsername(dto.getUsername());
		if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
			user.setPassword(dto.getPassword());
		}
		return userMapper.toDto(user);
	}

	@Transactional
	public void delete(UUID id) {
		if (!userRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
		}
		userRepository.deleteById(id);
	}
}
