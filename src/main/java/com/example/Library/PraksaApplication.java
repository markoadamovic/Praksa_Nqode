package com.example.Library;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PraksaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PraksaApplication.class, args);
	}

	@Bean
	public OpenAPI openAPIConfig() {
		return new OpenAPI().info(apiInfo());
	}

	public Info apiInfo() {
		Info info = new Info();
		info
				.title("Nqode Library")
				.description("Sample Documentation Generated Using Swagger for our Library Rest API")
				.version("V1");
		return info;
	}

}
