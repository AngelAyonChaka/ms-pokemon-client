package com.practice.ms_pokemon_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsPokemonClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPokemonClientApplication.class, args);
	}

}
