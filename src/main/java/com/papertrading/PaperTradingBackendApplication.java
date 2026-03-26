package com.papertrading;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.papertrading.model.Portfolio;
import com.papertrading.model.Stock;
import com.papertrading.model.User;
import com.papertrading.repository.PortfolioRepository;
import com.papertrading.repository.StockRepository;
import com.papertrading.repository.UserRepository;
import com.papertrading.util.AppConstants;

@SpringBootApplication
@EnableCaching
public class PaperTradingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaperTradingBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner seedData(
			StockRepository stockRepository,
			UserRepository userRepository,
			PortfolioRepository portfolioRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			if (stockRepository.count() == 0) {
				stockRepository.saveAll(List.of(
						Stock.builder().symbol("TCS").name("Tata Consultancy Services").exchange("NSE").price(4250.0).build(),
						Stock.builder().symbol("INFY").name("Infosys").exchange("NSE").price(1710.0).build(),
						Stock.builder().symbol("RELIANCE").name("Reliance Industries").exchange("NSE").price(2980.0).build(),
						Stock.builder().symbol("HDFCBANK").name("HDFC Bank").exchange("NSE").price(1635.0).build(),
						Stock.builder().symbol("SBIN").name("State Bank of India").exchange("NSE").price(820.0).build()));
			}

			User user = userRepository.findByUsername(AppConstants.DEMO_USERNAME).orElseGet(() -> userRepository.save(
					User.builder()
							.username(AppConstants.DEMO_USERNAME)
							.password(passwordEncoder.encode(AppConstants.DEMO_PASSWORD))
							.roles(Set.of(AppConstants.ROLE_USER))
							.cashBalance(AppConstants.DEFAULT_CASH_BALANCE)
							.build()));

			portfolioRepository.findByUserId(user.getId()).orElseGet(() -> portfolioRepository.save(
					Portfolio.builder()
							.userId(user.getId())
							.cashBalance(user.getCashBalance())
							.holdings(new ArrayList<>())
							.build()));
		};
	}
}
