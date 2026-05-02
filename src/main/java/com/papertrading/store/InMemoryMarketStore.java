package com.papertrading.store;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.papertrading.model.Portfolio;
import com.papertrading.model.Stock;
import com.papertrading.model.Trade;
import com.papertrading.model.TradeSide;
import com.papertrading.model.User;
import com.papertrading.util.AppConstants;

import jakarta.annotation.PostConstruct;

@Component
public class InMemoryMarketStore {

	private final Map<String, Stock> stocksBySymbol = new ConcurrentHashMap<>();
	private final Map<String, User> usersByUsername = new ConcurrentHashMap<>();
	private final Map<String, Portfolio> portfoliosByUserId = new ConcurrentHashMap<>();
	private final Map<String, Trade> tradesById = new ConcurrentHashMap<>();

	@PostConstruct
	public void seed() {
		if (stocksBySymbol.isEmpty()) {
			List<Stock> stocks = List.of(
					Stock.builder().id("stock-tcs").symbol("TCS").name("Tata Consultancy Services").exchange("NSE").price(4250.0).build(),
					Stock.builder().id("stock-infy").symbol("INFY").name("Infosys").exchange("NSE").price(1710.0).build(),
					Stock.builder().id("stock-reliance").symbol("RELIANCE").name("Reliance Industries").exchange("NSE").price(2980.0).build(),
					Stock.builder().id("stock-hdfc").symbol("HDFCBANK").name("HDFC Bank").exchange("NSE").price(1635.0).build(),
					Stock.builder().id("stock-sbin").symbol("SBIN").name("State Bank of India").exchange("NSE").price(820.0).build());
			stocks.forEach(stock -> stocksBySymbol.put(stock.getSymbol(), stock));
		}

		usersByUsername.computeIfAbsent(AppConstants.DEMO_USERNAME, username -> {
			User user = User.builder()
					.id("user-demo")
					.username(username)
					.password(AppConstants.DEMO_PASSWORD)
					.roles(Set.of(AppConstants.ROLE_USER))
					.cashBalance(AppConstants.DEFAULT_CASH_BALANCE)
					.createdAt(Instant.now())
					.build();
			portfoliosByUserId.putIfAbsent(user.getId(), Portfolio.builder()
					.id("portfolio-demo")
					.userId(user.getId())
					.cashBalance(user.getCashBalance())
					.holdings(new ArrayList<>())
					.build());
			return user;
		});
	}

	public List<Stock> getStocks() {
		return stocksBySymbol.values().stream()
				.sorted((left, right) -> left.getSymbol().compareToIgnoreCase(right.getSymbol()))
				.toList();
	}

	public Optional<Stock> findStockBySymbol(String symbol) {
		return Optional.ofNullable(stocksBySymbol.get(symbol.toUpperCase()));
	}

	public Optional<User> findUserByUsername(String username) {
		return Optional.ofNullable(usersByUsername.get(username));
	}

	public User saveUser(User user) {
		if (user.getCreatedAt() == null) {
			user.setCreatedAt(Instant.now());
		}
		usersByUsername.put(user.getUsername(), user);
		return user;
	}

	public Portfolio getOrCreatePortfolio(User user) {
		return portfoliosByUserId.computeIfAbsent(user.getId(), ignored -> Portfolio.builder()
				.id(UUID.randomUUID().toString())
				.userId(user.getId())
				.cashBalance(user.getCashBalance() == null ? AppConstants.DEFAULT_CASH_BALANCE : user.getCashBalance())
				.holdings(new ArrayList<>())
				.build());
	}

	public Portfolio savePortfolio(Portfolio portfolio) {
		portfoliosByUserId.put(portfolio.getUserId(), portfolio);
		return portfolio;
	}

	public Trade saveTrade(Trade trade) {
		if (trade.getId() == null || trade.getId().isBlank()) {
			trade.setId(UUID.randomUUID().toString());
		}
		tradesById.put(trade.getId(), trade);
		return trade;
	}

	public List<Trade> findTradesByUserId(String userId) {
		return tradesById.values().stream()
				.filter(trade -> userId.equals(trade.getUserId()))
				.toList();
	}

	public Portfolio applyTrade(String username, String symbol, int quantity, double price, TradeSide side) {
		User user = findUserByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
		Portfolio portfolio = getOrCreatePortfolio(user);
		double totalAmount = price * quantity;

		if (side == TradeSide.BUY) {
			if (user.getCashBalance() < totalAmount) {
				throw new IllegalArgumentException("Insufficient balance");
			}
			user.setCashBalance(user.getCashBalance() - totalAmount);
			upsertHolding(portfolio, symbol, quantity, price);
		} else {
			removeHolding(portfolio, symbol, quantity);
			user.setCashBalance(user.getCashBalance() + totalAmount);
		}

		portfolio.setCashBalance(user.getCashBalance());
		saveUser(user);
		savePortfolio(portfolio);
		return portfolio;
	}

	private void upsertHolding(Portfolio portfolio, String symbol, int quantity, double price) {
		List<Portfolio.Holding> holdings = new ArrayList<>(portfolio.getHoldings());
		Portfolio.Holding existing = holdings.stream()
				.filter(holding -> holding.getSymbol().equalsIgnoreCase(symbol))
				.findFirst()
				.orElse(null);

		if (existing == null) {
			holdings.add(Portfolio.Holding.builder()
					.symbol(symbol)
					.quantity(quantity)
					.averagePrice(price)
					.build());
		} else {
			int updatedQuantity = existing.getQuantity() + quantity;
			double updatedAverage = ((existing.getAveragePrice() * existing.getQuantity()) + (price * quantity)) / updatedQuantity;
			existing.setQuantity(updatedQuantity);
			existing.setAveragePrice(updatedAverage);
		}

		portfolio.setHoldings(holdings);
	}

	private void removeHolding(Portfolio portfolio, String symbol, int quantity) {
		List<Portfolio.Holding> holdings = new ArrayList<>(portfolio.getHoldings());
		Portfolio.Holding existing = holdings.stream()
				.filter(holding -> holding.getSymbol().equalsIgnoreCase(symbol))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No holding found for " + symbol));

		if (existing.getQuantity() < quantity) {
			throw new IllegalArgumentException("Insufficient stock quantity");
		}

		int remaining = existing.getQuantity() - quantity;
		if (remaining == 0) {
			holdings.remove(existing);
		} else {
			existing.setQuantity(remaining);
		}

		portfolio.setHoldings(holdings);
	}
}
