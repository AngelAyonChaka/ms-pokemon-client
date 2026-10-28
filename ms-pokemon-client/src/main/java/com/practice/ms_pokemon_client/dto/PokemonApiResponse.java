package com.practice.ms_pokemon_client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PokemonApiResponse(
		Integer id,
		String name,
		@JsonProperty("base_experience") Integer baseExperience,
		Integer height,
		Integer order,
		Integer weight,
		List<AbilitySlot> abilities) {
}
