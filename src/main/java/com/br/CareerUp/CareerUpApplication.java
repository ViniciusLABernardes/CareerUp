package com.br.CareerUp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class CareerUpApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareerUpApplication.class, args);
	}

}
