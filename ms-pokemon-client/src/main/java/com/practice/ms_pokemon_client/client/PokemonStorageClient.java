package com.practice.ms_pokemon_client.client;

import com.practice.ms_pokemon_client.dto.PokemonStorageRequest;
import com.practice.ms_pokemon_client.dto.PokemonStorageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pokemon-storage", url = "${storage-service.base-url}")
public interface PokemonStorageClient {

	@PostMapping("/api/pokemon")
	PokemonStorageResponse save(@RequestBody PokemonStorageRequest request);
}
