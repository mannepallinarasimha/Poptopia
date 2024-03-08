/**
 * 
 */
package com.kelloggs.promotions.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Add SpringConfiguration for the Swagger-UI
 * 
 * @author NARASIMHARAO MANNEPALLI (10700939)
 * @since 22st December 2023
 */
@Configuration
public class SpringConfigurattion {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().components(new Components()).info(new Info().title("Contact Application API")
				.description("This is a sample Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.")

		);
	}
}
