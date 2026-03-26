package com.papertrading.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.papertrading.security.JwtTokenProvider;

@Configuration
@EnableConfigurationProperties(JwtConfig.JwtProperties.class)
public class JwtConfig {

	@Bean
	JwtTokenProvider jwtTokenProvider(JwtProperties properties) {
		return new JwtTokenProvider(properties.getSecret(), properties.getExpirationMs());
	}

	@ConfigurationProperties(prefix = "app.jwt")
	public static class JwtProperties {
		private String secret;
		private long expirationMs;

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		public long getExpirationMs() {
			return expirationMs;
		}

		public void setExpirationMs(long expirationMs) {
			this.expirationMs = expirationMs;
		}
	}
}
