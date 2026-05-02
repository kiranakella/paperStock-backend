package com.papertrading.service.impl;

import java.time.Instant;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.papertrading.dto.request.LoginRequest;
import com.papertrading.dto.request.RegisterRequest;
import com.papertrading.dto.response.AuthResponse;
import com.papertrading.model.User;
import com.papertrading.security.JwtTokenProvider;
import com.papertrading.service.AuthService;
import com.papertrading.service.UserService;
import com.papertrading.util.DtoMapper;
import com.papertrading.util.AppConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public AuthResponse login(LoginRequest request) {
		// Check if demo credentials
		if (Objects.equals(AppConstants.DEMO_USERNAME, request.getUsername())
				&& Objects.equals(AppConstants.DEMO_PASSWORD, request.getPassword())) {
			User user = userService.getByUsername(AppConstants.DEMO_USERNAME);
			String token = jwtTokenProvider.generateToken(user);
			return DtoMapper.toAuthResponse(user, token);
		}

		// Try to find user in database
		User user = userService.findByUsername(request.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

		// Verify password
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Invalid credentials");
		}

		String token = jwtTokenProvider.generateToken(user);
		return DtoMapper.toAuthResponse(user, token);
	}

	@Override
	public AuthResponse register(RegisterRequest request) {
		// Validate input
		if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
			throw new IllegalArgumentException("Username is required");
		}
		if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
			throw new IllegalArgumentException("Password is required");
		}
		if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
			throw new IllegalArgumentException("Email is required");
		}

		// Check if username already exists
		if (userService.existsByUsername(request.getUsername())) {
			throw new IllegalArgumentException("Username already exists");
		}

		// Check if email already exists
		if (userService.existsByEmail(request.getEmail())) {
			throw new IllegalArgumentException("Email already exists");
		}

		// Create new user
		User newUser = User.builder()
				.id(UUID.randomUUID().toString())
				.username(request.getUsername())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.roles(Collections.singleton("ROLE_USER"))
				.cashBalance(100000.0) // Initial balance
				.isActive(true)
				.createdAt(Instant.now())
				.updatedAt(Instant.now())
				.build();

		// Save user
		User savedUser = userService.save(newUser);

		// Generate token
		String token = jwtTokenProvider.generateToken(savedUser);
		return DtoMapper.toAuthResponse(savedUser, token);
	}
}
