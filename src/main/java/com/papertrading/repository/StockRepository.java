package com.papertrading.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.papertrading.model.Stock;

public interface StockRepository extends MongoRepository<Stock, String> {
	List<Stock> findAllByOrderBySymbolAsc();
	Optional<Stock> findBySymbol(String symbol);
}
