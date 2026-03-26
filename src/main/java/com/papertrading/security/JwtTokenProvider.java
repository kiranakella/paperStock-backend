package com.papertrading.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class JwtTokenProvider {

	private final String secret;
	private final long expirationMs;

	public JwtTokenProvider(String secret, long expirationMs) {
		this.secret = secret;
		this.expirationMs = expirationMs;
	}

	public String generateToken(UserDetails userDetails) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + expirationMs);
		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(now)
				.setExpiration(expiryDate)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claimsResolver.apply(claims);
	}

	private SecretKey getSigningKey() {
		byte[] keyBytes = secret.length() >= 32
				? secret.getBytes(StandardCharsets.UTF_8)
				: Decoders.BASE64.decode(secret);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
