package com.practice.ms_pokemon_client.dto;

public record PokemonStorageResponse(
		Integer id,
		Integer pokemonId,
		String name,
		Integer baseExperience,
		Integer height,
		Integer order,
		Integer weight,
		String ability) {
}
