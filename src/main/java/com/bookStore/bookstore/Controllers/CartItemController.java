package com.bookStore.bookstore.Controllers;

import com.bookStore.bookstore.Models.dto.request.CartItemRequest;
import com.bookStore.bookstore.Models.dto.response.CartItemResponse;
import com.bookStore.bookstore.Services.CartItemService;
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
@RequestMapping("/api/cart-items")
public class CartItemController {

	private final CartItemService cartItemService;

	public CartItemController(CartItemService cartItemService) {
		this.cartItemService = cartItemService;
	}

	@GetMapping
	public List<CartItemResponse> getAllCartItems() {
		return cartItemService.findAll();
	}

	@GetMapping("/user/{userId}")
	public List<CartItemResponse> getCartItemsByUserId(@PathVariable UUID userId) {
		return cartItemService.findByUserId(userId);
	}

	@GetMapping("/{id}")
	public CartItemResponse getCartItemById(@PathVariable UUID id) {
		return cartItemService.findById(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CartItemResponse createCartItem(@Valid @RequestBody CartItemRequest request) {
		return cartItemService.create(request);
	}

	@PutMapping("/{id}")
	public CartItemResponse updateCartItem(@PathVariable UUID id, @Valid @RequestBody CartItemRequest request) {
		return cartItemService.update(id, request);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCartItem(@PathVariable UUID id) {
		cartItemService.delete(id);
	}
}
