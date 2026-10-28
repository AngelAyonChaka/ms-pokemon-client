package com.practice.ms_pokemon_client.exception;

import com.practice.ms_pokemon_client.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

	@ExceptionHandler(PokemonNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFound(
			PokemonNotFoundException exception, HttpServletRequest request) {
		return buildResponse(HttpStatus.NOT_FOUND, exception, request);
	}

	@ExceptionHandler(PokemonApiException.class)
	public ResponseEntity<ErrorResponse> handleApiError(
			PokemonApiException exception, HttpServletRequest request) {
		return buildResponse(HttpStatus.BAD_GATEWAY, exception, request);
	}

	@ExceptionHandler(PokemonConflictException.class)
	public ResponseEntity<ErrorResponse> handleConflict(
			PokemonConflictException exception, HttpServletRequest request) {
		return buildResponse(HttpStatus.CONFLICT, exception, request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleValidation(
			ConstraintViolationException exception, HttpServletRequest request) {
		return buildResponse(HttpStatus.BAD_REQUEST, exception, request);
	}

	private ResponseEntity<ErrorResponse> buildResponse(
			HttpStatus status, Exception exception, HttpServletRequest request) {
		return ResponseEntity.status(status).body(new ErrorResponse(
				Instant.now(),
				status.value(),
				status.getReasonPhrase(),
				exception.getMessage(),
				request.getRequestURI()));
	}
}
