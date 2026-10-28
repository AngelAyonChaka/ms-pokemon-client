package com.practice.ms_pokemon_client.service.impl;

import com.practice.ms_pokemon_client.client.PokemonApiClient;
import com.practice.ms_pokemon_client.client.PokemonStorageClient;
import com.practice.ms_pokemon_client.dto.AbilitySlot;
import com.practice.ms_pokemon_client.dto.PokemonApiResponse;
import com.practice.ms_pokemon_client.dto.PokemonStorageRequest;
import com.practice.ms_pokemon_client.dto.PokemonStorageResponse;
import com.practice.ms_pokemon_client.dto.StorageErrorResponse;
import com.practice.ms_pokemon_client.exception.PokemonApiException;
import com.practice.ms_pokemon_client.exception.PokemonConflictException;
import com.practice.ms_pokemon_client.exception.PokemonNotFoundException;
import com.practice.ms_pokemon_client.service.PokemonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PokemonServiceImpl implements PokemonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PokemonServiceImpl.class);

	private final PokemonApiClient pokemonApiClient;
	private final PokemonStorageClient pokemonStorageClient;
	private final ObjectMapper objectMapper;

	public PokemonServiceImpl(PokemonApiClient pokemonApiClient,
			PokemonStorageClient pokemonStorageClient, ObjectMapper objectMapper) {
		this.pokemonApiClient = pokemonApiClient;
		this.pokemonStorageClient = pokemonStorageClient;
		this.objectMapper = objectMapper;
	}

	@Override
	public PokemonStorageResponse findByName(String name) {
		LOGGER.info("Iniciando consulta del pokemon '{}'", name);
		try {
			PokemonApiResponse pokemon = pokemonApiClient.findByName(name);
			LOGGER.info("GET PokeAPI completado para '{}': id={}, nombre='{}'",
					name, pokemon == null ? null : pokemon.id(),
					pokemon == null ? null : pokemon.name());

			if (pokemon == null) {
				throw new PokemonApiException("PokeAPI devolvió una respuesta vacía", null);
			}

			AbilitySlot firstAbility = pokemon.abilities() == null
					? null
					: pokemon.abilities().stream().findFirst().orElse(null);

			PokemonStorageRequest request = new PokemonStorageRequest(
					pokemon.id(),
					pokemon.name(),
					pokemon.baseExperience(),
					pokemon.height(),
					pokemon.order(),
					pokemon.weight(),
					firstAbility == null || firstAbility.ability() == null
							? null
							: firstAbility.ability().name());

			LOGGER.info("Ejecutando POST al storage para pokemonId={}, nombre='{}'",
					request.pokemonId(), request.name());
			PokemonStorageResponse response = pokemonStorageClient.save(request);
			LOGGER.info("POST al storage completado para pokemonId={}, id generado={}",
					request.pokemonId(), response == null ? null : response.id());
			return response;
		} catch (FeignException.NotFound exception) {
			LOGGER.warn("Pokemon '{}' no encontrado en PokeAPI. status={}",
					name, exception.status());
			throw new PokemonNotFoundException(name, exception);
		} catch (FeignException exception) {
			if (exception.status() == 409) {
				String message = readStorageMessage(exception);
				LOGGER.warn(
						"Pokemon '{}' no se guardó porque ya existe. status={}, message='{}'",
						name, exception.status(), message);
				throw new PokemonConflictException(message, exception);
			}
			if (exception.status() >= 500) {
				LOGGER.error(
						"El servicio externo no está disponible para pokemon '{}'. status={}",
						name, exception.status());
				throw new PokemonApiException("Un servicio externo no está disponible", exception);
			}
			LOGGER.error("Error HTTP en servicio externo para pokemon '{}'. status={}",
					name, exception.status());
			throw new PokemonApiException("Error al consultar un servicio externo", exception);
		} catch (PokemonApiException | PokemonConflictException
				| PokemonNotFoundException exception) {
			LOGGER.warn("Flujo del pokemon '{}' finalizado: {}",
					name, exception.getMessage());
			throw exception;
		} catch (RuntimeException exception) {
			LOGGER.error("Error inesperado procesando pokemon '{}'", name, exception);
			throw new PokemonApiException("Error al consultar PokeAPI", exception);
		}
	}

	private String readStorageMessage(FeignException exception) {
		String responseBody = exception.contentUTF8();
		if (!responseBody.isBlank()) {
			try {
				StorageErrorResponse error = objectMapper.readValue(
						responseBody, StorageErrorResponse.class);
				if (error.message() != null && !error.message().isBlank()) {
					return error.message();
				}
			} catch (JsonProcessingException ignored) {
				// Se conserva un mensaje local si el servicio remoto no devuelve JSON válido.
			}
		}
		return "El pokemon ya fue guardado";
	}
}
