package com.practice.ms_pokemon_client.client;

import com.practice.ms_pokemon_client.dto.PokemonApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "poke-api", url = "${pokeapi.base-url}")
public interface PokemonApiClient {

	@GetMapping("/pokemon/{name}")
	PokemonApiResponse findByName(@PathVariable("name") String name);
}
