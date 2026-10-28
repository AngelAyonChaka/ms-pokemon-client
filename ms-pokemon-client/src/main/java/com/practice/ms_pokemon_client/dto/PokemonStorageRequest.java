package com.practice.ms_pokemon_client.dto;

public record PokemonStorageRequest(
		Integer pokemonId,
		String name,
		Integer baseExperience,
		Integer height,
		Integer order,
		Integer weight,
		String ability) {
}
