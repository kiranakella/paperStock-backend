package com.papertrading.util;

import java.util.List;

import com.papertrading.dto.response.AuthResponse;
import com.papertrading.dto.response.HoldingResponse;
import com.papertrading.dto.response.PortfolioResponse;
import com.papertrading.dto.response.StockResponse;
import com.papertrading.dto.response.TradeResponse;
import com.papertrading.dto.response.UserResponse;
import com.papertrading.model.Portfolio;
import com.papertrading.model.Stock;
import com.papertrading.model.Trade;
import com.papertrading.model.User;

public final class DtoMapper {

	private DtoMapper() {
	}

	public static StockResponse toStockResponse(Stock stock) {
		return StockResponse.builder()
				.symbol(stock.getSymbol())
				.name(stock.getName())
				.exchange(stock.getExchange())
				.price(stock.getPrice())
				.build();
	}

	public static TradeResponse toTradeResponse(Trade trade, String message) {
		return TradeResponse.builder()
				.id(trade.getId())
				.symbol(trade.getSymbol())
				.quantity(trade.getQuantity())
				.price(trade.getPrice())
				.amount(trade.getAmount())
				.side(trade.getSide().name())
				.executedAt(DateTimeUtil.toIso(trade.getCreatedAt()))
				.message(message)
				.build();
	}

	public static PortfolioResponse toPortfolioResponse(Portfolio portfolio) {
		List<HoldingResponse> holdings = portfolio.getHoldings().stream()
				.map(holding -> HoldingResponse.builder()
						.symbol(holding.getSymbol())
						.quantity(holding.getQuantity())
						.averagePrice(holding.getAveragePrice())
						.build())
				.toList();
		return PortfolioResponse.builder()
				.userId(portfolio.getUserId())
				.cashBalance(portfolio.getCashBalance())
				.holdings(holdings)
				.build();
	}

	public static AuthResponse toAuthResponse(User user, String token) {
		return AuthResponse.builder()
				.token(token)
				.tokenType("Bearer")
				.username(user.getUsername())
				.cashBalance(user.getCashBalance())
				.build();
	}

	public static UserResponse toUserResponse(User user) {
		return UserResponse.builder()
				.id(user.getId())
				.username(user.getUsername())
				.roles(user.getRoles())
				.cashBalance(user.getCashBalance())
				.build();
	}
}
