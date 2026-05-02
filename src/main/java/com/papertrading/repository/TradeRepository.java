package com.papertrading.repository;

import java.util.List;

import com.papertrading.model.Trade;

public interface TradeRepository {
	List<Trade> findByUserId(String userId);
	Trade save(Trade trade);
}
