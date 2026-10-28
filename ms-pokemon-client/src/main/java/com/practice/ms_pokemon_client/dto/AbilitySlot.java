package com.practice.ms_pokemon_client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AbilitySlot(
		@JsonProperty("is_hidden") Boolean hidden,
		Integer slot,
		AbilityDetail ability) {
}
