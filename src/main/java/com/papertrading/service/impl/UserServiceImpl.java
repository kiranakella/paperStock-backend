package com.papertrading.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.papertrading.model.Portfolio;
import com.papertrading.model.User;
import com.papertrading.repository.UserRepository;
import com.papertrading.store.InMemoryMarketStore;
import com.papertrading.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final InMemoryMarketStore marketStore;
	private final UserRepository userRepository;

	@Override
	public User getByUsername(String username) {
		return findByUsername(username)
				.orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
	}

	@Override
	public Optional<User> findByUsername(String username) {
		// Try MongoDB first
		Optional<User> user = userRepository.findByUsername(username);
		if (user.isPresent()) {
			return user;
		}
		// Fallback to in-memory store for demo user
		return marketStore.findUserByUsername(username);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username) || marketStore.findUserByUsername(username).isPresent();
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	@Override
	public Portfolio getOrCreatePortfolio(User user) {
		return marketStore.getOrCreatePortfolio(user);
	}

	@Override
	public Portfolio savePortfolio(Portfolio portfolio) {
		return marketStore.savePortfolio(portfolio);
	}
}
