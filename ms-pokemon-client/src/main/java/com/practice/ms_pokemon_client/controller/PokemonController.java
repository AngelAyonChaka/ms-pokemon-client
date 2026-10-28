package com.practice.ms_pokemon_client.controller;

import com.practice.ms_pokemon_client.dto.PokemonStorageResponse;
import com.practice.ms_pokemon_client.service.PokemonService;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/pokemon")
public class PokemonController {

	private final PokemonService pokemonService;

	public PokemonController(PokemonService pokemonService) {
		this.pokemonService = pokemonService;
	}

	@GetMapping("/{name}")
	public PokemonStorageResponse findByName(
			@PathVariable @Pattern(regexp = "^[a-zA-Z0-9-]+$",
					message = "El nombre del pokemon solo puede contener letras, números y guiones") String name) {
		return pokemonService.findByName(name);
	}
}
