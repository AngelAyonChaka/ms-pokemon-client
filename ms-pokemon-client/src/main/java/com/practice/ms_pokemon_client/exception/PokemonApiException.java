package com.practice.ms_pokemon_client.exception;

public class PokemonApiException extends RuntimeException {

	public PokemonApiException(String message, Throwable cause) {
		super(message, cause);
	}
}
