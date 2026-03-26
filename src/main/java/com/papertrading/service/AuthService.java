package com.papertrading.service;

import com.papertrading.dto.request.LoginRequest;
import com.papertrading.dto.response.AuthResponse;

public interface AuthService {
	AuthResponse login(LoginRequest request);
}
