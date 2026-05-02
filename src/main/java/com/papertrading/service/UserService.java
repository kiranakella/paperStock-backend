package com.papertrading.service;

import com.papertrading.model.Portfolio;
import com.papertrading.model.User;
import java.util.Optional;

public interface UserService {
	User getByUsername(String username);
	Optional<User> findByUsername(String username);
	Optional<User> findByEmail(String email);
	User save(User user);
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	Portfolio getOrCreatePortfolio(User user);
	Portfolio savePortfolio(Portfolio portfolio);
}
