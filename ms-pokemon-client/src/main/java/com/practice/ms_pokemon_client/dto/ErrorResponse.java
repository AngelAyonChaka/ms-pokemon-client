package com.practice.ms_pokemon_client.dto;

import java.time.Instant;

public record ErrorResponse(
		Instant timestamp,
		int status,
		String error,
		String message,
		String path) {
}
