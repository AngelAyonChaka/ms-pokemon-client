package com.practice.ms_pokemon_client.service;

import com.practice.ms_pokemon_client.dto.PokemonStorageResponse;

public interface PokemonService {

	PokemonStorageResponse findByName(String name);
}
