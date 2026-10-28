package com.practice.ms_pokemon_client.exception;

public class PokemonNotFoundException extends RuntimeException {

	public PokemonNotFoundException(String name, Throwable cause) {
		super("No se encontró el pokemon: " + name, cause);
	}
}
