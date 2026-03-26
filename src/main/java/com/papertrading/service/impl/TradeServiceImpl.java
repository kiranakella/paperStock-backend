package com.papertrading.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.papertrading.dto.request.TradeRequest;
import com.papertrading.dto.response.PortfolioResponse;
import com.papertrading.dto.response.TradeResponse;
import com.papertrading.model.Portfolio;
import com.papertrading.model.Portfolio.Holding;
import com.papertrading.model.Stock;
import com.papertrading.model.Trade;
import com.papertrading.model.TradeSide;
import com.papertrading.model.User;
import com.papertrading.repository.TradeRepository;
import com.papertrading.service.StockService;
import com.papertrading.service.TradeService;
import com.papertrading.service.UserService;
import com.papertrading.util.DateTimeUtil;
import com.papertrading.util.DtoMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {

	private final UserService userService;
	private final StockService stockService;
	private final TradeRepository tradeRepository;

	@Override
	public TradeResponse buyStock(String username, TradeRequest request) {
		return executeTrade(username, request, TradeSide.BUY);
	}

	@Override
	public TradeResponse sellStock(String username, TradeRequest request) {
		return executeTrade(username, request, TradeSide.SELL);
	}

	@Override
	public PortfolioResponse getPortfolio(String username) {
		User user = userService.getByUsername(username);
		Portfolio portfolio = userService.getOrCreatePortfolio(user);
		return PortfolioResponse.builder()
				.userId(portfolio.getUserId())
				.cashBalance(portfolio.getCashBalance())
				.holdings(portfolio.getHoldings().stream()
						.map(holding -> {
							double currentPrice = stockService.getStockPrice(holding.getSymbol());
							return com.papertrading.dto.response.HoldingResponse.builder()
									.symbol(holding.getSymbol())
									.quantity(holding.getQuantity())
									.averagePrice(holding.getAveragePrice())
									.currentPrice(currentPrice)
									.marketValue(currentPrice * holding.getQuantity())
									.build();
						})
						.toList())
				.build();
	}

	private TradeResponse executeTrade(String username, TradeRequest request, TradeSide side) {
		User user = userService.getByUsername(username);
		Portfolio portfolio = userService.getOrCreatePortfolio(user);
		Stock stock = stockService.getStockBySymbol(request.getSymbol());
		double price = stockService.getStockPrice(stock.getSymbol());
		double totalAmount = price * request.getQuantity();

		if (side == TradeSide.BUY) {
			if (user.getCashBalance() < totalAmount) {
				throw new IllegalArgumentException("Insufficient balance");
			}
			user.setCashBalance(user.getCashBalance() - totalAmount);
			updateHoldingForBuy(portfolio, stock, request.getQuantity(), price);
		} else {
			updateHoldingForSell(portfolio, stock, request.getQuantity());
			user.setCashBalance(user.getCashBalance() + totalAmount);
		}

		portfolio.setCashBalance(user.getCashBalance());
		userService.save(user);
		userService.savePortfolio(portfolio);

		Trade trade = Trade.builder()
				.userId(user.getId())
				.symbol(stock.getSymbol())
				.quantity(request.getQuantity())
				.price(price)
				.amount(totalAmount)
				.side(side)
				.createdAt(DateTimeUtil.now())
				.build();
		Trade saved = tradeRepository.save(trade);
		return DtoMapper.toTradeResponse(saved, side.name() + " order executed successfully");
	}

	private void updateHoldingForBuy(Portfolio portfolio, Stock stock, int quantity, double price) {
		List<Holding> holdings = new ArrayList<>(portfolio.getHoldings());
		Holding existing = holdings.stream()
				.filter(holding -> holding.getSymbol().equalsIgnoreCase(stock.getSymbol()))
				.findFirst()
				.orElse(null);

		if (existing == null) {
			holdings.add(Holding.builder()
					.symbol(stock.getSymbol())
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

	private void updateHoldingForSell(Portfolio portfolio, Stock stock, int quantity) {
		List<Holding> holdings = new ArrayList<>(portfolio.getHoldings());
		Holding existing = holdings.stream()
				.filter(holding -> holding.getSymbol().equalsIgnoreCase(stock.getSymbol()))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("No holding found for " + stock.getSymbol()));

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
