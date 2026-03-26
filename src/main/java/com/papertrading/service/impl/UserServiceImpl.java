package com.papertrading.service.impl;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.papertrading.model.Portfolio;
import com.papertrading.model.User;
import com.papertrading.repository.PortfolioRepository;
import com.papertrading.repository.UserRepository;
import com.papertrading.service.UserService;
import com.papertrading.util.AppConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PortfolioRepository portfolioRepository;

	@Override
	public User getByUsername(String username) {
		return userRepository.findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public Portfolio getOrCreatePortfolio(User user) {
		return portfolioRepository.findByUserId(user.getId())
				.orElseGet(() -> portfolioRepository.save(
						Portfolio.builder()
								.userId(user.getId())
								.cashBalance(user.getCashBalance() == null ? AppConstants.DEFAULT_CASH_BALANCE : user.getCashBalance())
								.holdings(new ArrayList<>())
								.build()));
	}

	@Override
	public Portfolio savePortfolio(Portfolio portfolio) {
		return portfolioRepository.save(portfolio);
	}
}
