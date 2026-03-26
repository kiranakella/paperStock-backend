package com.papertrading.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.papertrading.dto.request.LoginRequest;
import com.papertrading.dto.response.AuthResponse;
import com.papertrading.model.User;
import com.papertrading.security.JwtTokenProvider;
import com.papertrading.service.AuthService;
import com.papertrading.service.UserService;
import com.papertrading.util.DtoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserService userService;

	@Override
	public AuthResponse login(LoginRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user = userService.getByUsername(request.getUsername());
		String token = jwtTokenProvider.generateToken(user);
		return DtoMapper.toAuthResponse(user, token);
	}
}
