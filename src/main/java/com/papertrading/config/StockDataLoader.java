package com.papertrading.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.papertrading.model.Stock;
import com.papertrading.repository.StockRepository;

@Configuration
public class StockDataLoader {

	@Bean
	CommandLineRunner seedStocks(StockRepository stockRepository) {
		return args -> {
			if (stockRepository.count() > 0) {
				return;
			}

			stockRepository.saveAll(List.of(
					Stock.builder().symbol("TCS").name("Tata Consultancy Services").exchange("NSE").price(4250.0).build(),
					Stock.builder().symbol("RELIANCE").name("Reliance Industries").exchange("NSE").price(2980.0).build(),
					Stock.builder().symbol("INFY").name("Infosys").exchange("NSE").price(1710.0).build()));
		};
	}
}
