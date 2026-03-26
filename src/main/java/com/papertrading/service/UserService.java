package com.papertrading.service;

import com.papertrading.model.Portfolio;
import com.papertrading.model.User;

public interface UserService {
	User getByUsername(String username);
	User save(User user);
	Portfolio getOrCreatePortfolio(User user);
	Portfolio savePortfolio(Portfolio portfolio);
}
