package com.papertrading.graphql.resolver;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.papertrading.dto.request.LoginRequest;
import com.papertrading.dto.request.TradeRequest;
import com.papertrading.dto.response.AuthResponse;
import com.papertrading.dto.response.TradeResponse;
import com.papertrading.service.AuthService;
import com.papertrading.service.TradeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MutationResolver {

	private final AuthService authService;
	private final TradeService tradeService;

	@MutationMapping
	public AuthResponse login(@Argument LoginRequest request) {
		return authService.login(request);
	}

	@MutationMapping
	public TradeResponse buyStock(@Argument TradeRequest request, Authentication authentication) {
		requireAuthentication(authentication);
		return tradeService.buyStock(authentication.getName(), request);
	}

	@MutationMapping
	public TradeResponse sellStock(@Argument TradeRequest request, Authentication authentication) {
		requireAuthentication(authentication);
		return tradeService.sellStock(authentication.getName(), request);
	}

	private void requireAuthentication(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new IllegalArgumentException("Authentication required");
		}
	}
}
