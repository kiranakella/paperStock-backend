package com.papertrading.graphql.resolver;

import java.util.List;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.papertrading.dto.response.PortfolioResponse;
import com.papertrading.dto.response.StockResponse;
import com.papertrading.service.PortfolioService;
import com.papertrading.service.StockService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class QueryResolver {

	private final StockService stockService;
	private final PortfolioService portfolioService;

	@QueryMapping
	public List<StockResponse> getStocks() {
		return stockService.getStocks();
	}

	@QueryMapping
	public PortfolioResponse getPortfolio(Authentication authentication) {
		requireAuthentication(authentication);
		return portfolioService.getPortfolio(authentication.getName());
	}

	private void requireAuthentication(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new IllegalArgumentException("Authentication required");
		}
	}
}
