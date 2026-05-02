package com.papertrading.service.impl;

import org.springframework.stereotype.Service;

import com.papertrading.dto.request.TradeRequest;
import com.papertrading.dto.response.PortfolioResponse;
import com.papertrading.dto.response.TradeResponse;
import com.papertrading.model.Stock;
import com.papertrading.model.Trade;
import com.papertrading.model.TradeSide;
import com.papertrading.model.User;
import com.papertrading.store.InMemoryMarketStore;
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
	private final InMemoryMarketStore marketStore;

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
		return DtoMapper.toPortfolioResponse(marketStore.getOrCreatePortfolio(userService.getByUsername(username)));
	}

	private TradeResponse executeTrade(String username, TradeRequest request, TradeSide side) {
		User user = userService.getByUsername(username);
		Stock stock = stockService.getStockBySymbol(request.getSymbol());
		double price = stockService.getStockPrice(stock.getSymbol());
		double totalAmount = price * request.getQuantity();

		marketStore.applyTrade(username, stock.getSymbol(), request.getQuantity(), price, side);

		Trade trade = Trade.builder()
				.userId(user.getId())
				.symbol(stock.getSymbol())
				.quantity(request.getQuantity())
				.price(price)
				.amount(totalAmount)
				.side(side)
				.createdAt(DateTimeUtil.now())
				.build();
		Trade saved = marketStore.saveTrade(trade);
		return DtoMapper.toTradeResponse(saved, side.name() + " order executed successfully");
	}
}
