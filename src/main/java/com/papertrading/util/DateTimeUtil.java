package com.papertrading.util;

import java.time.Instant;

public final class DateTimeUtil {

	private DateTimeUtil() {
	}

	public static Instant now() {
		return Instant.now();
	}

	public static String toIso(Instant instant) {
		return instant == null ? null : instant.toString();
	}
}
