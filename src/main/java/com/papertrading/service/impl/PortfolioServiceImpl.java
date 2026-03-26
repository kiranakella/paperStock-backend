package com.papertrading.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.papertrading.dto.response.HoldingResponse;
import com.papertrading.dto.response.PortfolioResponse;
import com.papertrading.model.Portfolio;
import com.papertrading.model.User;
import com.papertrading.service.PortfolioService;
import com.papertrading.service.StockService;
import com.papertrading.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

	private final UserService userService;
	private final StockService stockService;

	@Override
	public PortfolioResponse getPortfolio(String username) {
		User user = userService.getByUsername(username);
		Portfolio portfolio = userService.getOrCreatePortfolio(user);
		List<HoldingResponse> holdings = portfolio.getHoldings().stream()
				.map(holding -> {
					double currentPrice = stockService.getStockPrice(holding.getSymbol());
					return HoldingResponse.builder()
							.symbol(holding.getSymbol())
							.quantity(holding.getQuantity())
							.averagePrice(holding.getAveragePrice())
							.currentPrice(currentPrice)
							.marketValue(currentPrice * holding.getQuantity())
							.build();
				})
				.toList();

		return PortfolioResponse.builder()
				.userId(portfolio.getUserId())
				.cashBalance(portfolio.getCashBalance())
				.holdings(holdings)
				.build();
	}

	@Override
	public Portfolio getOrCreatePortfolio(User user) {
		return userService.getOrCreatePortfolio(user);
	}

	@Override
	public Portfolio savePortfolio(Portfolio portfolio) {
		return userService.savePortfolio(portfolio);
	}
}
